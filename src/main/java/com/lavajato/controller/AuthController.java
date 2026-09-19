package com.lavajato.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.usuario.UsuarioLoginResponse;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.service.AuthService;
import com.lavajato.service.UsuarioService;
import com.lavajato.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioLoginResponse> login(@RequestBody Map<String, Object> dados) {
        return authService.validarLogin(dados);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody Map<String, Object> dados) {
        return usuarioService.cadastrarUsuario(dados);
    }
}
