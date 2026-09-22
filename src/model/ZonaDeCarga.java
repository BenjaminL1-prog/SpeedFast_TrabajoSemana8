package model;

import java.util.ArrayList;

public class ZonaDeCarga {

    private ArrayList<Pedido> pedidos;

    public ZonaDeCarga() {
        pedidos = new ArrayList<>();
    }

    public synchronized void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);

        System.out.println("[Zona de Carga] Pedido #"
                + String.format("%03d", pedido.getIdPedido())
                + " agregado.");
    }

    public synchronized Pedido retirarPedido() {

        for (int i = 0; i < pedidos.size(); i++) {

            Pedido pedido = pedidos.get(i);

            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {

                pedido.setEstado(EstadoPedido.EN_REPARTO);
                pedidos.remove(i);

                return pedido;
            }
        }

        return null;
    }

    public synchronized ArrayList<Pedido> getPedidos() {
        return new ArrayList<>(pedidos);
    }
}