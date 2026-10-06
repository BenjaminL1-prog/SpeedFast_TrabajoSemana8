package view;

import controller.ControladorPedidos;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VentanaListaPedidos extends JFrame {

    private ControladorPedidos controlador;
    private DefaultTableModel modeloTabla;
    private JTable tablaPedidos;

    public VentanaListaPedidos(
            ControladorPedidos controlador
    ) {

        this.controlador = controlador;

        setTitle("Lista de Pedidos - SpeedFast");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        actualizarTabla();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "LISTA DE PEDIDOS",
                SwingConstants.CENTER
        );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        add(titulo, BorderLayout.NORTH);

        String[] columnas = {
                "ID",
                "Dirección",
                "Tipo",
                "Distancia (km)",
                "Estado"
        };

        modeloTabla =
                new DefaultTableModel(
                        columnas,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };

        tablaPedidos =
                new JTable(modeloTabla);

        tablaPedidos.setRowHeight(25);

        tablaPedidos
                .getTableHeader()
                .setReorderingAllowed(false);

        JScrollPane scrollPane =
                new JScrollPane(tablaPedidos);

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        JButton botonActualizar =
                new JButton("Actualizar tabla");

        botonActualizar.addActionListener(
                e -> actualizarTabla()
        );

        JPanel panelBoton =
                new JPanel();

        panelBoton.add(botonActualizar);

        add(
                panelBoton,
                BorderLayout.SOUTH
        );
    }

    private void actualizarTabla() {

        modeloTabla.setRowCount(0);

        ArrayList<Pedido> pedidos =
                controlador.listarPedidosDesdeDB();

        for (Pedido pedido : pedidos) {

            String tipo =
                    obtenerTipoPedido(pedido);

            Object[] fila = {
                    pedido.getIdPedido(),
                    pedido.getDireccionEntrega(),
                    tipo,
                    pedido.getDistanciaKm(),
                    pedido.getEstado()
            };

            modeloTabla.addRow(fila);
        }
    }

    private String obtenerTipoPedido(
            Pedido pedido
    ) {

        String nombreClase =
                pedido.getClass().getSimpleName();

        switch (nombreClase) {

            case "PedidoComida":
                return "Comida";

            case "PedidoEncomienda":
                return "Encomienda";

            case "PedidoExpress":
                return "Express";

            default:
                return "Desconocido";
        }
    }
}