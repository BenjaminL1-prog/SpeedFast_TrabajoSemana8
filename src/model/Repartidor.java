package model;

import dao.EntregaDAO;
import dao.PedidoDAO;

import java.time.LocalDate;
import java.time.LocalTime;

public class Repartidor implements Runnable {

    private int id;
    private String nombre;
    private ZonaDeCarga zonaDeCarga;

    public Repartidor(
            String nombre,
            ZonaDeCarga zonaDeCarga
    ) {

        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    public Repartidor(
            int id,
            String nombre,
            ZonaDeCarga zonaDeCarga
    ) {

        this.id = id;
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ZonaDeCarga getZonaDeCarga() {
        return zonaDeCarga;
    }

    public void setZonaDeCarga(ZonaDeCarga zonaDeCarga) {
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {

        while (true) {

            Pedido pedido =
                    zonaDeCarga.retirarPedido();

            if (pedido == null) {
                break;
            }

            System.out.println(
                    "[Repartidor: " + nombre + "] "
                            + "retiró Pedido #"
                            + String.format(
                            "%03d",
                            pedido.getIdPedido()
                    )
                            + " - Estado: "
                            + pedido.getEstado()
            );

            try {

                int tiempoEntrega = 2000;

                System.out.println(
                        "[Repartidor: " + nombre + "] "
                                + "Entregando Pedido #"
                                + String.format(
                                "%03d",
                                pedido.getIdPedido()
                        )
                                + "..."
                );

                Thread.sleep(tiempoEntrega);

                pedido.setEstado(
                        EstadoPedido.ENTREGADO
                );

                System.out.println(
                        "[Repartidor: " + nombre + "] "
                                + "Pedido #"
                                + String.format(
                                "%03d",
                                pedido.getIdPedido()
                        )
                                + " entregado. Estado: "
                                + pedido.getEstado()
                );

                PedidoDAO pedidoDAO =
                        new PedidoDAO();

                boolean estadoActualizado =
                        pedidoDAO.actualizarEstado(
                                pedido.getIdPedido(),
                                pedido.getEstado().name()
                        );

                if (estadoActualizado) {

                    System.out.println(
                            "[MySQL] Estado del Pedido #"
                                    + String.format(
                                    "%03d",
                                    pedido.getIdPedido()
                            )
                                    + " actualizado a ENTREGADO."
                    );

                } else {

                    System.out.println(
                            "[MySQL] No se pudo actualizar "
                                    + "el estado del Pedido #"
                                    + String.format(
                                    "%03d",
                                    pedido.getIdPedido()
                            )
                    );
                }

                Entrega entrega =
                        new Entrega(
                                pedido.getIdPedido(),
                                id,
                                LocalDate.now(),
                                LocalTime.now()
                        );

                EntregaDAO entregaDAO =
                        new EntregaDAO();

                boolean guardada =
                        entregaDAO.guardar(entrega);

                if (guardada) {

                    System.out.println(
                            "[MySQL] Entrega del Pedido #"
                                    + String.format(
                                    "%03d",
                                    pedido.getIdPedido()
                            )
                                    + " registrada correctamente."
                    );

                } else {

                    System.out.println(
                            "[MySQL] No se pudo registrar "
                                    + "la entrega del Pedido #"
                                    + String.format(
                                    "%03d",
                                    pedido.getIdPedido()
                            )
                    );
                }

            } catch (InterruptedException e) {

                System.out.println(
                        "[Repartidor: " + nombre + "] "
                                + "Entrega interrumpida."
                );

                Thread.currentThread().interrupt();

                break;
            }
        }

        System.out.println(
                "[Repartidor: " + nombre
                        + "] No quedan pedidos disponibles. "
                        + "Finalizó su jornada."
        );
    }
}