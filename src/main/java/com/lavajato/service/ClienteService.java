package com.lavajato.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.cliente.ClienteResponse;
import com.lavajato.model.Cliente;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.VeiculoRepository;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    VeiculoRepository veiculoRepository;
    
    public ClienteResponse cadastrarCliente(Map<String, Object> dados) {
        String nome = (String) dados.get("nome");
        String celular = (String) dados.get("celular");
        Integer fidelidade = (Integer) dados.get("fidelidade");

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCelular(celular);
        cliente.setFidelidade(fidelidade);
        cliente = clienteRepository.save(cliente);

        return clienteToClienteResponse(cliente);
    }

    public ClienteResponse atualizarCliente(Map<String, Object> dados) {
        Long id = Long.parseLong(dados.get("id").toString());
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return null;
        }
        
        String nome = (String) dados.get("nome");
        String celular = (String) dados.get("celular");
        Integer fidelidade = (Integer) dados.get("fidelidade");

        if (nome != null) {
            cliente.setNome(nome);
        }
        if (celular != null) {
            cliente.setCelular(celular);
        }
        if (fidelidade != null) {
            cliente.setFidelidade(fidelidade);
        }

        cliente = clienteRepository.save(cliente);
        
        return clienteToClienteResponse(cliente);
    }

    public ClienteResponse buscarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null)
            return null;
        else 
            return clienteToClienteResponse(cliente);
    }

    public List<ClienteResponse> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAllByOrderById();
        return clientes.stream().map(cliente -> {
            return clienteToClienteResponse(cliente);
        }).toList();
    }

    public boolean deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente != null) {
            clienteRepository.delete(cliente);
            return true;
        }
        return false;
    }

    private ClienteResponse clienteToClienteResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getCelular(), cliente.getFidelidade(), cliente.getVeiculos());
    }
}
