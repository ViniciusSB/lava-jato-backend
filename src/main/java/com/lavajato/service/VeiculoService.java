package com.lavajato.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<VeiculoResponse> atualizarVeiculo(Map<String, Object> dados) {
        Long id = Long.parseLong(dados.get("id").toString());
        Veiculo veiculo = veiculoRepository.findById(id).orElse(null);
        VeiculoResponse vr = new VeiculoResponse();
        if (veiculo != null) {
            Long qtdOrdens = veiculoRepository.qtdOrdensPorVeiculoId(id);
            String tipo = (String) dados.get("tipo");
            if (qtdOrdens > 0 && !tipo.equals(veiculo.getTipo().toString())) {
                vr.setMensagem("Erro ao alterar. O veículo tem uma ou mais ordem de serviço associada(s)");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(vr);
            }

            String modelo = (String) dados.get("modelo");
            String marca = (String) dados.get("marca");
            String cor = (String) dados.get("cor");
            String placa = (String) dados.get("placa");
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
            vr = veiculoToVeiculoResponse(veiculo);
            return ResponseEntity.ok(vr);
        } else {
            vr.setMensagem("Veículo não cadastrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(vr);
        }
    }

    public VeiculoResponse buscarVeiculo(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id).orElse(null);
        if (veiculo != null) {
            return veiculoToVeiculoResponse(veiculo);
        }
        return null;
    }

    public List<VeiculoResponse> listarTodos() {
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return veiculos.stream().map(v -> {
            return veiculoToVeiculoResponse(v);
        }).collect(Collectors.toList());
    }

    public List<VeiculoResponse> listarVeiculosPorClienteId(Long clienteId) {
        List<Veiculo> veiculos = veiculoRepository.findByClienteId(clienteId);
        return veiculos.stream().map(veiculo -> {
            return veiculoToVeiculoResponse(veiculo);
        }).collect(Collectors.toList());
    }

    public ResponseEntity<VeiculoResponse> deletarVeiculo(Long id) {
        VeiculoResponse vr = new VeiculoResponse();
        if (veiculoRepository.existsById(id)) {
            Long qtdOrdens = veiculoRepository.qtdOrdensPorVeiculoId(id);
            if (qtdOrdens > 0) {
                vr.setMensagem("Erro ao excluir. O veículo tem uma ou mais ordem de serviço associada(s)");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(vr);
            }
            veiculoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private VeiculoResponse veiculoToVeiculoResponse(Veiculo veiculo) {
        VeiculoResponse response = new VeiculoResponse();
        response.setId(veiculo.getId());
        response.setModelo(veiculo.getModelo());
        response.setMarca(veiculo.getMarca());
        response.setCor(veiculo.getCor());
        response.setPlaca(veiculo.getPlaca());
        response.setTipo(veiculo.getTipo() != null ? veiculo.getTipo().toString() : null);
        response.setClienteId(veiculo.getCliente() != null ? veiculo.getCliente().getId() : null);
        response.setClienteNome(veiculo.getCliente() != null ? veiculo.getCliente().getNome() : null);
        return response;
    }

}
