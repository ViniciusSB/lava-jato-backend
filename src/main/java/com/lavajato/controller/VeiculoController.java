package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.veiculo.VeiculoResponse;
import com.lavajato.model.Veiculo;
import com.lavajato.service.VeiculoService;


@RestController
@RequestMapping("/veiculo")
public class VeiculoController {
    
    @Autowired
    VeiculoService veiculoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Veiculo> adicionarVeiculoClienteId(@RequestBody Map<String, Object> dados) {
        Veiculo veiculo = veiculoService.adicionarVeiculoClienteId(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculo);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Veiculo> atualizarVeiculo(@RequestBody Map<String, Object> dados) {
        Veiculo veiculo = veiculoService.atualizarVeiculo(dados);
        return ResponseEntity.ok(veiculo);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<VeiculoResponse> listarVeiculoPorId(@PathVariable Long id) {
        VeiculoResponse veiculo = veiculoService.buscarVeiculo(id);
        if (veiculo != null) {
            return ResponseEntity.ok(veiculo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<VeiculoResponse>> listarTodos() {
        List<VeiculoResponse> veiculos = veiculoService.listarTodos();
        if (veiculos != null) {
            return ResponseEntity.ok(veiculos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar/cliente/{clienteId}")
    public ResponseEntity<List<VeiculoResponse>> listarVeiculosPorClienteId(@PathVariable Long clienteId) {
        List<VeiculoResponse> veiculos = veiculoService.listarVeiculosPorClienteId(clienteId);
        if (veiculos != null) {
            return ResponseEntity.ok(veiculos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        boolean deletado = veiculoService.deletarVeiculo(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
