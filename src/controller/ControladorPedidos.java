package controller;

import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;
import model.ZonaDeCarga;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ControladorPedidos {

    private ArrayList<Pedido> pedidos;
    private ZonaDeCarga zonaDeCarga;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    public ControladorPedidos() {

        pedidos = new ArrayList<>();
        zonaDeCarga = new ZonaDeCarga();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        cargarPedidosDesdeDB();
    }

    private void cargarPedidosDesdeDB() {

        List<Pedido> pedidosBD =
                pedidoDAO.listarTodos();

        for (Pedido pedido : pedidosBD) {

            pedidos.add(pedido);

            if (pedido.getEstado()
                    == model.EstadoPedido.PENDIENTE) {

                zonaDeCarga.agregarPedido(pedido);
            }
        }
    }

    public boolean agregarPedido(
            String direccion,
            String tipo
    ) {

        Pedido nuevoPedido;

        switch (tipo.toLowerCase()) {

            case "comida":

                nuevoPedido = new PedidoComida(
                        0,
                        direccion,
                        0.0
                );

                break;

            case "encomienda":

                nuevoPedido = new PedidoEncomienda(
                        0,
                        direccion,
                        0.0
                );

                break;

            case "express":

                nuevoPedido = new PedidoExpress(
                        0,
                        direccion,
                        0.0
                );

                break;

            default:
                return false;
        }

        boolean guardado =
                pedidoDAO.guardar(nuevoPedido);

        if (!guardado) {
            return false;
        }

        pedidos.add(nuevoPedido);
        zonaDeCarga.agregarPedido(nuevoPedido);

        return true;
    }

    public ArrayList<Pedido> getPedidos() {

        return pedidos;
    }

    public ArrayList<Pedido> listarPedidosDesdeDB() {

        return new ArrayList<>(
                pedidoDAO.listarTodos()
        );
    }

    public ZonaDeCarga getZonaDeCarga() {

        return zonaDeCarga;
    }

    public Pedido buscarPedidoPorId(
            int idPedido
    ) {

        for (Pedido pedido : pedidos) {

            if (pedido.getIdPedido()
                    == idPedido) {

                return pedido;
            }
        }

        return null;
    }

    public void iniciarEntregas() {

        List<Repartidor> repartidores =
                repartidorDAO.listarTodos();

        if (repartidores.isEmpty()) {

            System.out.println(
                    "No hay repartidores registrados "
                            + "en MySQL."
            );

            return;
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        repartidores.size()
                );

        for (Repartidor repartidor :
                repartidores) {

            repartidor.setZonaDeCarga(
                    zonaDeCarga
            );

            executor.submit(repartidor);
        }

        executor.shutdown();

        new Thread(() -> {

            try {

                if (!executor.awaitTermination(
                        30,
                        TimeUnit.SECONDS
                )) {

                    System.out.println(
                            "El proceso de entregas "
                                    + "tardó demasiado."
                    );

                    executor.shutdownNow();
                }

            } catch (InterruptedException e) {

                System.out.println(
                        "El proceso de entregas "
                                + "fue interrumpido."
                );

                executor.shutdownNow();

                Thread.currentThread().interrupt();
            }

        }).start();
    }
}