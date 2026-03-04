package com.clientes.ui;

import com.clientes.model.Cliente;
import com.clientes.service.ClienteService;

import javax.swing.*;
import java.awt.*;

public class ClienteForm extends JFrame {

    private JTextField txtNombre = new JTextField(15);
    private JTextField txtEdad = new JTextField(5);
    private JTextField txtCiudad = new JTextField(10);
    private JComboBox<String> comboTipo =
            new JComboBox<>(new String[] {"Regular", "Premium", "VIP"});

    private ClienteService service = new ClienteService();

    public ClienteForm() {

        setTitle("Registro de Clientes");
        setSize(400, 250);
        setLayout(new GridLayout(6,2));

        add(new JLabel("Nombre:"));
        add(txtNombre);

        add(new JLabel("Edad:"));
        add(txtEdad);

        add(new JLabel("Ciudad:"));
        add(txtCiudad);

        add(new JLabel("Tipo Cliente:"));
        add(comboTipo);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnGrafico = new JButton("Ver Gráfico");

        add(btnGuardar);
        add(btnGrafico);

        btnGuardar.addActionListener(e -> guardar());
        btnGrafico.addActionListener(e -> new GraficoClientes());

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void guardar() {
        try {
            Cliente cliente = new Cliente(
                    txtNombre.getText(),
                    Integer.parseInt(txtEdad.getText()),
                    txtCiudad.getText(),
                    comboTipo.getSelectedItem().toString()
            );

            service.registrarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente registrado");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
