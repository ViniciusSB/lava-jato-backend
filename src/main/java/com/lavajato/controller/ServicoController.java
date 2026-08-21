package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.model.Servico;
import com.lavajato.service.ServicoService;

@RestController
@RequestMapping("/servico")
public class ServicoController {

    @Autowired
    ServicoService servicoService;
    
    @GetMapping("/listar")
    public ResponseEntity<List<Servico>> listarTodos() {
        List<Servico> servicos = servicoService.listar();
        return ResponseEntity.ok(servicos);
    }

    @PostMapping("/criar") 
    public ResponseEntity<Servico> criar(@RequestBody Map<String, Object> dados) {
        Servico servico = servicoService.criar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    @PutMapping("/atualizar") 
    public ResponseEntity<Servico> atualizar(@RequestBody Map<String, Object> dados) {
        Servico servico = servicoService.criar(dados);
        return ResponseEntity.status(HttpStatus.OK).body(servico);
    }

    @PutMapping("/deletar") 
    public ResponseEntity<Void> deletar(Long id) {
        boolean sucesso = servicoService.deletar(id);
        if (sucesso)
            return ResponseEntity.noContent().build();
        else 
            return ResponseEntity.notFound().build();
        
    }
}
