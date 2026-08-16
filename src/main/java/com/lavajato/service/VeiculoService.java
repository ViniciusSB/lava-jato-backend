package com.lavajato.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.veiculo.VeiculoResponse;
import com.lavajato.model.Cliente;
import com.lavajato.model.Veiculo;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.VeiculoRepository;

@Service
public class VeiculoService {

    @Autowired
    VeiculoRepository veiculoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    public Veiculo adicionarVeiculoClienteId(Map<String, Object> dados) {
        Long clienteId = dados.get("clienteId") != null ? Long.valueOf(dados.get("clienteId").toString()) : null;
        String modelo = (String) dados.get("modelo");
        String marca = (String) dados.get("marca");
        String cor = (String) dados.get("cor");
        String placa = (String) dados.get("placa");
        String tipo = (String) dados.get("tipo");

        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);

        Veiculo veiculo = new Veiculo();
        veiculo.setCliente(cliente);
        veiculo.setModelo(modelo);
        veiculo.setMarca(marca);
        veiculo.setCor(cor);
        veiculo.setPlaca(placa);
        
        if (tipo != null) {
            Veiculo.tipoVeiculo tipoVeiculo = Veiculo.tipoVeiculo.valueOf(tipo.toUpperCase());
            veiculo.setTipo(tipoVeiculo);
        }

        veiculo = veiculoRepository.save(veiculo);

        return veiculo;
    }

    public Veiculo atualizarVeiculo(Long id, Map<String, Object> dados) {
        Veiculo veiculo = veiculoRepository.findById(id).orElse(null);
        if (veiculo != null) {
            String modelo = (String) dados.get("modelo");
            String marca = (String) dados.get("marca");
            String cor = (String) dados.get("cor");
            String placa = (String) dados.get("placa");
            String tipo = (String) dados.get("tipo");
            Long clienteId = dados.get("clienteId") != null ? Long.valueOf(dados.get("clienteId").toString()) : null;

            if (clienteId != null) {
                Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
                veiculo.setCliente(cliente);
            }
            if (modelo != null) 
                veiculo.setModelo(modelo);
            if (marca != null) 
                veiculo.setMarca(marca);
            if (cor != null) 
                veiculo.setCor(cor);
            if (placa != null) 
                veiculo.setPlaca(placa);
            if (tipo != null) {
                Veiculo.tipoVeiculo tipoVeiculo = Veiculo.tipoVeiculo.valueOf(tipo.toUpperCase());
                veiculo.setTipo(tipoVeiculo);
            }

            veiculo = veiculoRepository.save(veiculo);
        }

        return veiculo;
    }

    public VeiculoResponse buscarVeiculo(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id).orElse(null);
        if (veiculo != null) {
            VeiculoResponse response = new VeiculoResponse();
            response.setId(veiculo.getId());
            response.setModelo(veiculo.getModelo());
            response.setMarca(veiculo.getMarca());
            response.setCor(veiculo.getCor());
            response.setPlaca(veiculo.getPlaca());
            response.setTipo(veiculo.getTipo() != null ? veiculo.getTipo().toString() : null);
            response.setClienteId(veiculo.getCliente() != null ? veiculo.getCliente().getId() : null);
            return response;
        }
        return null;
    }

    public List<VeiculoResponse> listarTodos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return veiculos.stream().map(v -> {
            VeiculoResponse response = new VeiculoResponse();
            response.setId(v.getId());
            response.setModelo(v.getModelo());
            response.setMarca(v.getMarca());
            response.setCor(v.getCor());
            response.setPlaca(v.getPlaca());
            response.setTipo(v.getTipo() != null ? v.getTipo().toString() : null);
            response.setClienteId(v.getCliente() != null ? v.getCliente().getId() : null);
            return response;
        }).collect(Collectors.toList());
    }

    public List<VeiculoResponse> listarVeiculosPorClienteId(Long clienteId) {
        List<Veiculo> veiculos = veiculoRepository.findByClienteId(clienteId);
        return veiculos.stream().map(veiculo -> {
            VeiculoResponse response = new VeiculoResponse();
            response.setId(veiculo.getId());
            response.setModelo(veiculo.getModelo());
            response.setMarca(veiculo.getMarca());
            response.setCor(veiculo.getCor());
            response.setPlaca(veiculo.getPlaca());
            response.setTipo(veiculo.getTipo() != null ? veiculo.getTipo().toString() : null);
            response.setClienteId(veiculo.getCliente() != null ? veiculo.getCliente().getId() : null);
            return response;
        }).collect(Collectors.toList());
    }

    public boolean deletarVeiculo(Long id) {
        if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
