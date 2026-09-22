package view;

import controller.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private ControladorPedidos controlador;

    public VentanaPrincipal() {

        this(new ControladorPedidos());
    }

    public VentanaPrincipal(ControladorPedidos controlador) {

        this.controlador = controlador;

        setTitle("SISTEMA DE REPARTOS SPEEDFAST");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
                "SISTEMA DE REPARTOS SPEEDFAST",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 22));

        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 1, 10, 10));

        JButton botonRegistrar = new JButton("Registrar pedido");
        JButton botonListar = new JButton("Listar pedidos");
        JButton botonEntregas = new JButton("Asignar repartidor / Iniciar entrega");

        panelBotones.add(botonRegistrar);
        panelBotones.add(botonListar);
        panelBotones.add(botonEntregas);

        add(panelBotones, BorderLayout.CENTER);

        botonRegistrar.addActionListener(e -> {

            VentanaRegistroPedido ventana =
                    new VentanaRegistroPedido(controlador);

            ventana.setVisible(true);
        });

        botonListar.addActionListener(e -> {

            VentanaListaPedidos ventana =
                    new VentanaListaPedidos(controlador);

            ventana.setVisible(true);
        });

        botonEntregas.addActionListener(e -> {

            iniciarEntregas();
        });
    }

    private void iniciarEntregas() {

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea asignar repartidores e iniciar las entregas?",
                "Iniciar entregas",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            controlador.iniciarEntregas();

            JOptionPane.showMessageDialog(
                    this,
                    "Los repartidores fueron asignados.\n"
                            + "Las entregas han comenzado.",
                    "Entregas iniciadas",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}