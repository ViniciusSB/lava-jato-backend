package com.lavajato.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioRequest;
import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioResponse;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteRequestDTO;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO;
import com.lavajato.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    DashboardService dashboardService;

    @PostMapping("/funcionario/{id}") 
    public ResponseEntity<DashboardFuncionarioResponse> dashboardFuncionarioId(@PathVariable Long id, @RequestBody DashboardFuncionarioRequest request) {
        DashboardFuncionarioResponse informacoes = dashboardService.dashboardFuncionario(id, request);
        return ResponseEntity.ok().body(informacoes);
    }

    @PostMapping("/gerente") 
    public ResponseEntity<DashboardGerenteResponseDTO> dashboardGerente(@RequestBody DashboardGerenteRequestDTO request) {
        DashboardGerenteResponseDTO informacoes = dashboardService.dashboardGerente(request);
        return ResponseEntity.ok().body(informacoes);
    }
    
}
