package com.clientes.service;

import com.clientes.dao.ClienteDAO;
import com.clientes.model.Cliente;

import java.util.Map;

public class ClienteService {

    private ClienteDAO dao = new ClienteDAO();

    public void registrarCliente(Cliente cliente) throws Exception {
        dao.guardarCliente(cliente);
    }

    public Map<String, Integer> obtenerEstadisticas() throws Exception {
        return dao.obtenerEstadisticas();
    }
}   
