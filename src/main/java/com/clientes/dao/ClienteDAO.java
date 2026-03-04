package com.clientes.dao;

import com.clientes.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ClienteDAO {

    public void guardarCliente(Cliente cliente) throws Exception {

        String sql = "INSERT INTO CLIENTE (NOMBRE, EDAD, CIUDAD, TIPO_CLIENTE) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setInt(2, cliente.getEdad());
            ps.setString(3, cliente.getCiudad());
            ps.setString(4, cliente.getTipoCliente());

            ps.executeUpdate();
        }
    }

    public Map<String, Integer> obtenerEstadisticas() throws Exception {

        String sql = """
            SELECT TIPO_CLIENTE, COUNT(*) TOTAL 
            FROM CLIENTE
            GROUP BY TIPO_CLIENTE
        """;

        Map<String, Integer> datos = new HashMap<>();

        try (Connection conn = ConexionBD.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                datos.put(rs.getString("TIPO_CLIENTE"), rs.getInt("TOTAL"));
            }
        }

        return datos;
    }
}   
