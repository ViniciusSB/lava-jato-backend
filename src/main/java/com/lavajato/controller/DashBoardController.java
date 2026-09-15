package com.lavajato.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioRequest;
import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioResponse;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteRequestDTO;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO;
import com.lavajato.model.Usuario;
import com.lavajato.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    DashboardService dashboardService;

    @PostMapping("/funcionario/{id}")
    public ResponseEntity<DashboardFuncionarioResponse> dashboardFuncionarioId(@PathVariable Long id,
            @RequestBody DashboardFuncionarioRequest request, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("FUNCIONARIO") || user.getTipoUsuario().toString().equals("ADM")) {
            DashboardFuncionarioResponse informacoes = dashboardService.dashboardFuncionario(id, request);
            return ResponseEntity.ok().body(informacoes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/gerente")
    public ResponseEntity<DashboardGerenteResponseDTO> dashboardGerente(
            @RequestBody DashboardGerenteRequestDTO request, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("GERENTE") || user.getTipoUsuario().toString().equals("ADM")) {
            DashboardGerenteResponseDTO informacoes = dashboardService.dashboardGerente(request);
            return ResponseEntity.ok().body(informacoes);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
