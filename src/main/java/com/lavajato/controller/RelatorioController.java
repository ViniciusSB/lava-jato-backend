package com.lavajato.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.relatorio.RelatorioRequest;
import com.lavajato.model.Usuario;
import com.lavajato.service.RelatorioService;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    RelatorioService relatorioService;

    @PostMapping("/funcionario")
    public ResponseEntity<byte[]> gerarRelatorioFuncionario(@RequestBody RelatorioRequest request,
            Authentication authentication) throws Exception {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("FUNCIONARIO") || user.getTipoUsuario().toString().equals("ADM")) {
            byte[] relatorio = relatorioService.gerarRelatorioFuncionario(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(relatorio);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/faturamento")
    public ResponseEntity<byte[]> gerarRelatorioFaturamento(@RequestBody RelatorioRequest request,
            Authentication authentication) throws Exception {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            byte[] relatorio = relatorioService.gerarRelatorioFaturamento(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(relatorio);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/clientes")
    public ResponseEntity<byte[]> gerarRelatorioClientes(@RequestBody RelatorioRequest request,
            Authentication authentication) throws Exception {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            byte[] relatorio = relatorioService.gerarRelatorioClientes(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(relatorio);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/funcionarios")
    public ResponseEntity<byte[]> gerarRelatorioFuncionarios(@RequestBody RelatorioRequest request,
            Authentication authentication) throws Exception {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            byte[] relatorio = relatorioService.gerarRelatorioFuncionarios(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(relatorio);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
