package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.model.Servico;
import com.lavajato.model.Usuario;
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
    public ResponseEntity<Servico> criar(@RequestBody Map<String, Object> dados, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            Servico servico = servicoService.criar(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(servico);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Servico> atualizar(@RequestBody Map<String, Object> dados, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            Servico servico = servicoService.atualizar(dados);
            return ResponseEntity.status(HttpStatus.OK).body(servico);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            boolean sucesso = servicoService.deletar(id);
            if (sucesso)
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
