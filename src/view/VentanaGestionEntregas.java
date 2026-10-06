package view;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VentanaGestionEntregas extends JFrame {

    private JComboBox<String> comboPedido;
    private JComboBox<String> comboRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private EntregaDAO entregaDAO;
    private PedidoDAO pedidoDAO;
    private RepartidorDAO repartidorDAO;

    private List<Pedido> pedidos;
    private List<Repartidor> repartidores;

    private DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateTimeFormatter formatoHora =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public VentanaGestionEntregas() {

        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        setTitle("Gestión de Entregas");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarDatosCombos();
        cargarEntregas();
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
                        new GridLayout(5, 2, 5, 5)
                );

        JLabel lblPedido =
                new JLabel("Pedido:");

        comboPedido =
                new JComboBox<>();

        JLabel lblRepartidor =
                new JLabel("Repartidor:");

        comboRepartidor =
                new JComboBox<>();

        JLabel lblFecha =
                new JLabel("Fecha:");

        txtFecha =
                new JTextField();

        JLabel lblHora =
                new JLabel("Hora:");

        txtHora =
                new JTextField();

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnEditar =
                new JButton("Editar");

        panelFormulario.add(lblPedido);
        panelFormulario.add(comboPedido);

        panelFormulario.add(lblRepartidor);
        panelFormulario.add(comboRepartidor);

        panelFormulario.add(lblFecha);
        panelFormulario.add(txtFecha);

        panelFormulario.add(lblHora);
        panelFormulario.add(txtHora);

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnEditar);

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Pedido",
                                "Repartidor",
                                "Fecha",
                                "Hora"
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

        tablaEntregas =
                new JTable(modeloTabla);

        JScrollPane scroll =
                new JScrollPane(tablaEntregas);

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

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
                registrarEntrega()
        );

        btnEditar.addActionListener(e ->
                editarEntrega()
        );

        btnEliminar.addActionListener(e ->
                eliminarEntrega()
        );

        btnLimpiar.addActionListener(e ->
                limpiarFormulario()
        );

        tablaEntregas
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tablaEntregas
                                        .getSelectedRow();

                        if (fila >= 0) {

                            cargarDatosFormulario(
                                    fila
                            );
                        }
                    }
                });
    }

    private void cargarDatosCombos() {

        comboPedido.removeAllItems();

        pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            comboPedido.addItem(
                    pedido.getIdPedido()
                            + " - "
                            + pedido.getDireccionEntrega()
            );
        }

        comboRepartidor.removeAllItems();

        repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            comboRepartidor.addItem(
                    repartidor.getId()
                            + " - "
                            + repartidor.getNombre()
            );
        }
    }

    private void registrarEntrega() {

        if (comboPedido.getSelectedIndex() < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (comboRepartidor.getSelectedIndex() < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un repartidor.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String fechaTexto =
                txtFecha.getText().trim();

        String horaTexto =
                txtHora.getText().trim();

        if (fechaTexto.isEmpty()
                || horaTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar fecha y hora.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(
                            fechaTexto,
                            formatoFecha
                    );

            LocalTime hora =
                    LocalTime.parse(
                            horaTexto,
                            formatoHora
                    );

            int idPedido =
                    obtenerIdPedidoSeleccionado();

            int idRepartidor =
                    obtenerIdRepartidorSeleccionado();

            Entrega entrega =
                    new Entrega(
                            idPedido,
                            idRepartidor,
                            fecha,
                            hora
                    );

            boolean registrada =
                    entregaDAO.create(
                            entrega
                    );

            if (registrada) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega registrada correctamente."
                );

                limpiarFormulario();
                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar la entrega.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n\n"
                            + "Fecha: yyyy-MM-dd\n"
                            + "Hora: HH:mm:ss",
                    "Error de formato",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void editarEntrega() {

        int fila =
                tablaEntregas
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega de la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (comboPedido.getSelectedIndex() < 0
                || comboRepartidor.getSelectedIndex() < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar pedido y repartidor.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha.getText().trim(),
                            formatoFecha
                    );

            LocalTime hora =
                    LocalTime.parse(
                            txtHora.getText().trim(),
                            formatoHora
                    );

            int id =
                    Integer.parseInt(
                            modeloTabla
                                    .getValueAt(
                                            fila,
                                            0
                                    )
                                    .toString()
                    );

            int idPedido =
                    obtenerIdPedidoSeleccionado();

            int idRepartidor =
                    obtenerIdRepartidorSeleccionado();

            Entrega entrega =
                    new Entrega(
                            idPedido,
                            idRepartidor,
                            fecha,
                            hora
                    );

            entrega.setId(id);

            boolean actualizado =
                    entregaDAO.update(
                            entrega
                    );

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente."
                );

                limpiarFormulario();
                cargarEntregas();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar la entrega.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n\n"
                            + "Fecha: yyyy-MM-dd\n"
                            + "Hora: HH:mm:ss",
                    "Error de formato",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void eliminarEntrega() {

        int fila =
                tablaEntregas
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega de la tabla.",
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
                                + "la entrega #" + id + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                entregaDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente."
            );

            limpiarFormulario();
            cargarEntregas();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarEntregas() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas =
                entregaDAO.readAll();

        for (Entrega entrega :
                entregas) {

            String nombreRepartidor =
                    obtenerNombreRepartidor(
                            entrega.getIdRepartidor()
                    );

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getIdPedido(),
                            nombreRepartidor,
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void cargarDatosFormulario(
            int fila
    ) {

        int idPedido =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        1
                                )
                                .toString()
                );

        int idRepartidor =
                obtenerIdRepartidorPorNombre(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        2
                                )
                                .toString()
                );

        comboPedido.setSelectedItem(
                obtenerTextoPedido(
                        idPedido
                )
        );

        comboRepartidor.setSelectedItem(
                obtenerTextoRepartidor(
                        idRepartidor
                )
        );

        txtFecha.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                3
                        )
                        .toString()
        );

        txtHora.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                4
                        )
                        .toString()
        );
    }

    private int obtenerIdPedidoSeleccionado() {

        String seleccionado =
                comboPedido
                        .getSelectedItem()
                        .toString();

        return Integer.parseInt(
                seleccionado
                        .split(" - ")[0]
        );
    }

    private int obtenerIdRepartidorSeleccionado() {

        String seleccionado =
                comboRepartidor
                        .getSelectedItem()
                        .toString();

        return Integer.parseInt(
                seleccionado
                        .split(" - ")[0]
        );
    }

    private String obtenerNombreRepartidor(
            int id
    ) {

        for (Repartidor repartidor :
                repartidores) {

            if (repartidor.getId() == id) {

                return repartidor.getNombre();
            }
        }

        return "ID " + id;
    }

    private int obtenerIdRepartidorPorNombre(
            String nombre
    ) {

        for (Repartidor repartidor :
                repartidores) {

            if (repartidor.getNombre()
                    .equals(nombre)) {

                return repartidor.getId();
            }
        }

        return -1;
    }

    private String obtenerTextoPedido(
            int id
    ) {

        for (Pedido pedido : pedidos) {

            if (pedido.getIdPedido() == id) {

                return pedido.getIdPedido()
                        + " - "
                        + pedido.getDireccionEntrega();
            }
        }

        return null;
    }

    private String obtenerTextoRepartidor(
            int id
    ) {

        for (Repartidor repartidor :
                repartidores) {

            if (repartidor.getId() == id) {

                return repartidor.getId()
                        + " - "
                        + repartidor.getNombre();
            }
        }

        return null;
    }

    private void limpiarFormulario() {

        if (comboPedido.getItemCount() > 0) {
            comboPedido.setSelectedIndex(0);
        }

        if (comboRepartidor.getItemCount() > 0) {
            comboRepartidor.setSelectedIndex(0);
        }

        txtFecha.setText("");
        txtHora.setText("");

        tablaEntregas.clearSelection();
    }
}