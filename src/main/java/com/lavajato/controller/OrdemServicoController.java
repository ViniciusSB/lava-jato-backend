package com.lavajato.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.model.OrdemServico;
import com.lavajato.service.OrdemServicoService;

@RestController
@RequestMapping("/ordemServico")
public class OrdemServicoController {

    @Autowired
    OrdemServicoService ordemServicoService;
    
    @PostMapping("/gerar")
    public ResponseEntity<OrdemServico> criarOrdemServico(@RequestBody Map<String, Object> dados) {
        OrdemServico ordemServico = ordemServicoService.gerar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServico);
    }
}
