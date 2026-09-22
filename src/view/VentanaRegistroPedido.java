package view;

import controller.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private ControladorPedidos controlador;

    private JTextField campoId;
    private JTextField campoDireccion;
    private JComboBox<String> comboTipo;

    public VentanaRegistroPedido(ControladorPedidos controlador) {

        this.controlador = controlador;

        setTitle("Registrar Pedido - SpeedFast");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "REGISTRAR NUEVO PEDIDO",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel etiquetaId = new JLabel("ID:");
        JLabel etiquetaDireccion = new JLabel("Dirección:");
        JLabel etiquetaTipo = new JLabel("Tipo:");

        campoId = new JTextField();
        campoDireccion = new JTextField();

        comboTipo = new JComboBox<>(
                new String[]{
                        "comida",
                        "encomienda",
                        "express"
                }
        );

        panelFormulario.add(etiquetaId);
        panelFormulario.add(campoId);

        panelFormulario.add(etiquetaDireccion);
        panelFormulario.add(campoDireccion);

        panelFormulario.add(etiquetaTipo);
        panelFormulario.add(comboTipo);

        add(panelFormulario, BorderLayout.CENTER);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonVolver = new JButton("Volver al menú");

        botonGuardar.addActionListener(e -> guardarPedido());

        botonVolver.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();

        panelBotones.add(botonGuardar);
        panelBotones.add(botonVolver);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarPedido() {

        String textoId = campoId.getText().trim();
        String direccion = campoDireccion.getText().trim();
        String tipo = comboTipo.getSelectedItem().toString();

        if (textoId.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int id;

        try {

            id = Integer.parseInt(textoId);

            if (id <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser un número mayor que cero.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        boolean agregado = controlador.agregarPedido(
                id,
                direccion,
                tipo
        );

        if (!agregado) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el pedido.\n"
                            + "Verifique que el ID no esté repetido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Pedido registrado correctamente.",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE
        );

        campoId.setText("");
        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
    }
}