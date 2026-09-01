package com.lavajato.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.service.AdminService;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    AdminService service;
    
    @PostMapping("/gerarOrdemServicoTeste")
    public ResponseEntity<String> gerarOrdensServico(@RequestBody Map<String, Object> dados) {
        String retorno = service.gerarOrdensServico(dados);

        return ResponseEntity.status(HttpStatus.OK).body(retorno);
    }
}
