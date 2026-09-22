package controller;

import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;
import model.ZonaDeCarga;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ControladorPedidos {

    private ArrayList<Pedido> pedidos;
    private ZonaDeCarga zonaDeCarga;

    public ControladorPedidos() {
        pedidos = new ArrayList<>();
        zonaDeCarga = new ZonaDeCarga();

        cargarPedidosIniciales();
    }

    private void cargarPedidosIniciales() {

        PedidoComida pedidoComida1 = new PedidoComida(
                1,
                "Av. Providencia 1234",
                4.0
        );

        PedidoComida pedidoComida2 = new PedidoComida(
                2,
                "Av. Apoquindo 456",
                3.0
        );

        PedidoEncomienda pedidoEncomienda1 = new PedidoEncomienda(
                3,
                "Av. Las Condes 2456",
                6.0
        );

        PedidoEncomienda pedidoEncomienda2 = new PedidoEncomienda(
                4,
                "Av. Kennedy 1234",
                5.0
        );

        PedidoExpress pedidoExpress1 = new PedidoExpress(
                5,
                "Av. Vicuña Mackenna 789",
                7.0
        );

        PedidoExpress pedidoExpress2 = new PedidoExpress(
                6,
                "Av. Grecia 321",
                4.0
        );

        agregarPedidoInicial(pedidoComida1);
        agregarPedidoInicial(pedidoComida2);
        agregarPedidoInicial(pedidoEncomienda1);
        agregarPedidoInicial(pedidoEncomienda2);
        agregarPedidoInicial(pedidoExpress1);
        agregarPedidoInicial(pedidoExpress2);
    }

    private void agregarPedidoInicial(Pedido pedido) {
        pedidos.add(pedido);
        zonaDeCarga.agregarPedido(pedido);
    }

    public boolean agregarPedido(int idPedido, String direccion, String tipo) {

        if (buscarPedidoPorId(idPedido) != null) {
            return false;
        }

        Pedido nuevoPedido;

        switch (tipo.toLowerCase()) {

            case "comida":
                nuevoPedido = new PedidoComida(
                        idPedido,
                        direccion,
                        0.0
                );
                break;

            case "encomienda":
                nuevoPedido = new PedidoEncomienda(
                        idPedido,
                        direccion,
                        0.0
                );
                break;

            case "express":
                nuevoPedido = new PedidoExpress(
                        idPedido,
                        direccion,
                        0.0
                );
                break;

            default:
                return false;
        }

        pedidos.add(nuevoPedido);
        zonaDeCarga.agregarPedido(nuevoPedido);

        return true;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public ZonaDeCarga getZonaDeCarga() {
        return zonaDeCarga;
    }

    public Pedido buscarPedidoPorId(int idPedido) {

        for (Pedido pedido : pedidos) {

            if (pedido.getIdPedido() == idPedido) {
                return pedido;
            }
        }

        return null;
    }

    public int obtenerSiguienteId() {

        int mayorId = 0;

        for (Pedido pedido : pedidos) {

            if (pedido.getIdPedido() > mayorId) {
                mayorId = pedido.getIdPedido();
            }
        }

        return mayorId + 1;
    }

    public void iniciarEntregas() {

        Repartidor camila = new Repartidor(
                "Camila",
                zonaDeCarga
        );

        Repartidor luis = new Repartidor(
                "Luis",
                zonaDeCarga
        );

        Repartidor pedro = new Repartidor(
                "Pedro",
                zonaDeCarga
        );

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(camila);
        executor.submit(luis);
        executor.submit(pedro);

        executor.shutdown();

        new Thread(() -> {

            try {

                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {

                    System.out.println(
                            "El proceso de entregas tardó demasiado."
                    );

                    executor.shutdownNow();
                }

            } catch (InterruptedException e) {

                System.out.println(
                        "El proceso de entregas fue interrumpido."
                );

                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }

        }).start();
    }
}