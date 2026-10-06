package view;

import controller.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private ControladorPedidos controlador;

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
                new GridLayout(2, 2, 10, 10)
        );

        panelFormulario.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel etiquetaDireccion = new JLabel("Dirección:");
        JLabel etiquetaTipo = new JLabel("Tipo:");

        campoDireccion = new JTextField();

        comboTipo = new JComboBox<>(
                new String[]{
                        "comida",
                        "encomienda",
                        "express"
                }
        );

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

        String direccion = campoDireccion.getText().trim();
        String tipo = comboTipo.getSelectedItem().toString();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar una dirección.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean agregado = controlador.agregarPedido(
                direccion,
                tipo
        );

        if (!agregado) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el pedido.",
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

        campoDireccion.setText("");
        comboTipo.setSelectedIndex(0);
    }
}