package com.lavajato.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.faturamento.FaturamentoResponse;
import com.lavajato.service.FaturamentoService;

@RestController
@RequestMapping("/faturamento")
public class FaturamentoController {

    @Autowired
    FaturamentoService faturamentoService;
    
    @GetMapping("/listar")
    public ResponseEntity<List<FaturamentoResponse>> listar() {
        List<FaturamentoResponse> responses = faturamentoService.listarFaturamentos();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<FaturamentoResponse> listarPorId(@PathVariable Long id) {
        FaturamentoResponse response = faturamentoService.listarPorId(id);
        if (response != null)
            return ResponseEntity.ok().body(response);
        else
            return ResponseEntity.notFound().build();
    }
}
