package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.MensagemResponse;
import com.lavajato.dto.servico.ServicoResponse;
import com.lavajato.model.Usuario;
import com.lavajato.service.ServicoService;

@RestController
@RequestMapping("/servico")
public class ServicoController {

    @Autowired
    ServicoService servicoService;

    @GetMapping("/listar")
    public ResponseEntity<List<ServicoResponse>> listarTodos() {
        List<ServicoResponse> servicos = servicoService.listar();
        return ResponseEntity.ok(servicos);
    }

    @PostMapping("/criar")
    public ResponseEntity<ServicoResponse> criar(@RequestBody Map<String, Object> dados, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            ServicoResponse servico = servicoService.criar(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(servico);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ServicoResponse> atualizar(@RequestBody Map<String, Object> dados, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            ServicoResponse servico = servicoService.atualizar(dados);
            return ResponseEntity.status(HttpStatus.OK).body(servico);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping ("/desativar/{id}")
    public ResponseEntity<MensagemResponse> desativarServico(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getTipoUsuario().toString().equals("GERENTE")) {
            return servicoService.desativarServico(id);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping ("/ativar/{id}")
    public ResponseEntity<MensagemResponse> ativarServico(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getTipoUsuario().toString().equals("GERENTE")) {
            return servicoService.ativarServico(id);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
