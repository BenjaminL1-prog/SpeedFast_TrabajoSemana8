package view;

import dao.RepartidorDAO;
import model.Repartidor;
import model.ZonaDeCarga;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGestionRepartidores extends JFrame {

    private JTextField txtNombre;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;

    private RepartidorDAO repartidorDAO;

    public VentanaGestionRepartidores() {

        repartidorDAO = new RepartidorDAO();

        setTitle("Gestión de Repartidores");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearInterfaz();
        cargarRepartidores();
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
                new JPanel(new FlowLayout());

        JLabel lblNombre =
                new JLabel("Nombre:");

        txtNombre =
                new JTextField(20);

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        JButton btnLimpiar =
                new JButton("Limpiar");

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnEditar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nombre"
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

        tablaRepartidores =
                new JTable(modeloTabla);

        JScrollPane scroll =
                new JScrollPane(tablaRepartidores);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.NORTH
        );

        panelPrincipal.add(
                scroll,
                BorderLayout.CENTER
        );

        add(panelPrincipal);

        btnRegistrar.addActionListener(e ->
                registrarRepartidor()
        );

        btnEditar.addActionListener(e ->
                editarRepartidor()
        );

        btnEliminar.addActionListener(e ->
                eliminarRepartidor()
        );

        btnLimpiar.addActionListener(e ->
                limpiarFormulario()
        );

        tablaRepartidores
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tablaRepartidores
                                        .getSelectedRow();

                        if (fila >= 0) {

                            txtNombre.setText(
                                    modeloTabla
                                            .getValueAt(
                                                    fila,
                                                    1
                                            )
                                            .toString()
                            );
                        }
                    }
                });
    }

    private void registrarRepartidor() {

        String nombre =
                txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(
                        nombre,
                        new ZonaDeCarga()
                );

        boolean registrado =
                repartidorDAO.create(
                        repartidor
                );

        if (registrado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente."
            );

            limpiarFormulario();
            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editarRepartidor() {

        int fila =
                tablaRepartidores
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor de la tabla.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre =
                txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre.",
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

        Repartidor repartidor =
                new Repartidor(
                        id,
                        nombre,
                        new ZonaDeCarga()
                );

        boolean actualizado =
                repartidorDAO.update(
                        repartidor
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente."
            );

            limpiarFormulario();
            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarRepartidor() {

        int fila =
                tablaRepartidores
                        .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor de la tabla.",
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

        String nombre =
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString();

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar a "
                                + nombre
                                + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado =
                repartidorDAO.delete(id);

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente."
            );

            limpiarFormulario();
            cargarRepartidores();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el repartidor.\n"
                            + "Es posible que tenga entregas asociadas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarRepartidores() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor :
                repartidores) {

            modeloTabla.addRow(
                    new Object[]{
                            repartidor.getId(),
                            repartidor.getNombre()
                    }
            );
        }
    }

    private void limpiarFormulario() {

        txtNombre.setText("");

        tablaRepartidores
                .clearSelection();
    }
}