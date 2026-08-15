package com.lavajato.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.model.Cliente;
import com.lavajato.model.Veiculo;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.VeiculoRepository;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    VeiculoRepository veiculoRepository;
    
    public Cliente cadastrarCliente(Map<String, Object> dados) {
        String nome = (String) dados.get("nome");
        String celular = (String) dados.get("celular");
        List<Veiculo> veiculos = new ArrayList<>();
        Integer fidelidade = (Integer) dados.get("fidelidade");

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCelular(celular);
        cliente.setFidelidade(fidelidade);
        cliente = clienteRepository.save(cliente);
        
        if (dados.get("veiculos") != null) {
            List<Map<String, Object>> veiculosData = (List<Map<String, Object>>) dados.get("veiculos");
            for (Map<String, Object> veiculoData : veiculosData) {
                Veiculo veiculo = new Veiculo();
                veiculo.setCliente(cliente);
                veiculo.setModelo((String) veiculoData.get("modelo"));
                veiculo.setMarca((String) veiculoData.get("marca"));
                veiculo.setCor((String) veiculoData.get("cor"));
                veiculo.setPlaca((String) veiculoData.get("placa"));
                String tipo = (String) veiculoData.get("tipo");
                if (tipo != null) {
                    Veiculo.tipoVeiculo tipoVeiculo = Veiculo.tipoVeiculo.valueOf(tipo.toUpperCase());
                    veiculo.setTipo(tipoVeiculo);
                }
                veiculos.add(veiculo);
            }
        }
        
        veiculoRepository.saveAll(veiculos);
        System.out.println("Cliente cadastrado com sucesso: " + cliente.getId() + " - " + cliente.getNome());
        return cliente;
    }

    public Cliente atualizarCliente(Long id, Map<String, Object> dados) {
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
        
        System.out.println("Cliente atualizado com sucesso: " + cliente.getId() + " - " + cliente.getNome());
        return cliente;
    }

    public Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public boolean deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente != null) {
            clienteRepository.delete(cliente);
            return true;
        }
        return false;
    }
}
