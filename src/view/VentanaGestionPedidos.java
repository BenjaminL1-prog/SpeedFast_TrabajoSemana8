package view;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionPedidos extends JFrame {

    private JTextField txtDireccion;
    private JComboBox<String> comboTipo;
    private JComboBox<String> comboEstado;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private PedidoDAO pedidoDAO;

    public VentanaGestionPedidos() {

        pedidoDAO = new PedidoDAO();

        setTitle("Gestión de Pedidos");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarPedidos();
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(4, 2, 5, 5)
                );

        JLabel lblDireccion =
                new JLabel("Dirección:");

        txtDireccion =
                new JTextField();

        JLabel lblTipo =
                new JLabel("Tipo:");

        comboTipo =
                new JComboBox<>(
                        new String[]{
                                "COMIDA",
                                "ENCOMIENDA",
                                "EXPRESS"
                        }
                );

        JLabel lblEstado =
                new JLabel("Estado:");

        comboEstado =
                new JComboBox<>(
                        new String[]{
                                "PENDIENTE",
                                "EN_REPARTO",
                                "ENTREGADO"
                        }
                );

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        panelFormulario.add(lblDireccion);
        panelFormulario.add(txtDireccion);

        panelFormulario.add(lblTipo);
        panelFormulario.add(comboTipo);

        panelFormulario.add(lblEstado);
        panelFormulario.add(comboEstado);

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnEditar);

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Dirección",
                                "Tipo",
                                "Estado"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int fila,
                            int columna
                    ) {
                        return false;
                    }
                };

        tablaPedidos =
                new JTable(modeloTabla);

        JScrollPane scroll =
                new JScrollPane(tablaPedidos);

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout()
                );

        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                scroll,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        btnRegistrar.addActionListener(e ->
                registrarPedido()
        );

        btnEditar.addActionListener(e ->
                editarPedido()
        );

        btnEliminar.addActionListener(e ->
                eliminarPedido()
        );

        btnLimpiar.addActionListener(e ->
                limpiarFormulario()
        );

        tablaPedidos
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tablaPedidos
                                        .getSelectedRow();

                        if (fila >= 0) {

                            cargarDatosFormulario(
                                    fila
                            );
                        }
                    }
                });
    }

    private void registrarPedido() {

        String direccion =
                txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String tipo =
                comboTipo
                        .getSelectedItem()
                        .toString();

        String estado =
                comboEstado
                        .getSelectedItem()
                        .toString();

        Pedido pedido =
                crearPedido(
                        0,
                        direccion,
                        tipo
                );

        if (pedido == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Tipo de pedido no válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        pedido.setEstado(estado);

        boolean registrado =
                pedidoDAO.create(pedido);

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente."
            );

            limpiarFormulario();
            cargarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editarPedido() {

        int fila =
                tablaPedidos
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido de la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String direccion =
                txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );

        String tipo =
                comboTipo
                        .getSelectedItem()
                        .toString();

        String estado =
                comboEstado
                        .getSelectedItem()
                        .toString();

        Pedido pedido =
                crearPedido(
                        id,
                        direccion,
                        tipo
                );

        if (pedido == null) {
            return;
        }

        pedido.setEstado(estado);

        boolean actualizado =
                pedidoDAO.update(pedido);

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente."
            );

            limpiarFormulario();
            cargarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarPedido() {

        int fila =
                tablaPedidos
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido de la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar "
                                + "el pedido #" + id + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                pedidoDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente."
            );

            limpiarFormulario();
            cargarPedidos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el pedido.\n"
                            + "Es posible que tenga una entrega asociada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarPedidos() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getIdPedido(),
                            pedido.getDireccionEntrega(),
                            obtenerTipo(pedido),
                            pedido.getEstado().name()
                    }
            );
        }
    }

    private void cargarDatosFormulario(
            int fila
    ) {

        txtDireccion.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString()
        );

        comboTipo.setSelectedItem(
                modeloTabla
                        .getValueAt(
                                fila,
                                2
                        )
                        .toString()
        );

        comboEstado.setSelectedItem(
                modeloTabla
                        .getValueAt(
                                fila,
                                3
                        )
                        .toString()
        );
    }

    private Pedido crearPedido(
            int id,
            String direccion,
            String tipo
    ) {

        switch (tipo) {

            case "COMIDA":

                return new PedidoComida(
                        id,
                        direccion,
                        0.0
                );

            case "ENCOMIENDA":

                return new PedidoEncomienda(
                        id,
                        direccion,
                        0.0
                );

            case "EXPRESS":

                return new PedidoExpress(
                        id,
                        direccion,
                        0.0
                );

            default:

                return null;
        }
    }

    private String obtenerTipo(
            Pedido pedido
    ) {

        if (pedido instanceof PedidoComida) {
            return "COMIDA";
        }

        if (pedido instanceof PedidoEncomienda) {
            return "ENCOMIENDA";
        }

        if (pedido instanceof PedidoExpress) {
            return "EXPRESS";
        }

        return "DESCONOCIDO";
    }

    private void limpiarFormulario() {

        txtDireccion.setText("");

        comboTipo.setSelectedIndex(0);

        comboEstado.setSelectedIndex(0);

        tablaPedidos.clearSelection();
    }
}