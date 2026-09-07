package com.lavajato.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.service.RelatorioService;

@RestController 
@RequestMapping("/relatorio")
public class RelatorioController {

    @Autowired
    RelatorioService relatorioService;
    
    @GetMapping("/clientes")
    public ResponseEntity<byte[]> gerarRelatorioClientes() throws Exception {
        byte [] relatorio = relatorioService.gerarRelatorioClientes();

        return ResponseEntity.ok()
                
                .contentType(MediaType.APPLICATION_PDF)
                .body(relatorio);
    }
}
