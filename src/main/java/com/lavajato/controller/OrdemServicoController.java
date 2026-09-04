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

import com.lavajato.dto.ordemServico.OrdemServicoPaginadoResponse;
import com.lavajato.dto.ordemServico.OrdemServicoResponse;
import com.lavajato.service.OrdemServicoService;

@RestController
@RequestMapping("/ordemServico")
public class OrdemServicoController {

    @Autowired
    OrdemServicoService ordemServicoService;
    
    @PostMapping("/gerar")
    public ResponseEntity<OrdemServicoResponse> criarOrdemServico(@RequestBody Map<String, Object> dados) {
        OrdemServicoResponse response = ordemServicoService.gerar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<OrdemServicoResponse> atualizarOrdemServico(@RequestBody Map<String, Object> dados) {
        OrdemServicoResponse response = ordemServicoService.atualizar(dados);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/listar")
    public ResponseEntity<OrdemServicoPaginadoResponse> listarOrdemServico(@RequestBody Map<String, Object> filtros) {
        OrdemServicoPaginadoResponse responses = ordemServicoService.listar(filtros);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<OrdemServicoResponse> atualizarOrdemServico(@PathVariable Long id) {
        OrdemServicoResponse response = ordemServicoService.listarPorId(id);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<OrdemServicoResponse> deletarOrdemServico(@PathVariable Long id) {
        boolean deletado = ordemServicoService.deletar(id);
        if (deletado) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
