package com.lavajato.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.model.Usuario;
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
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> dados) {
        Usuario usuario = authService.validarLogin(dados);
        if (usuario != null) {
            String email = (String) dados.get("email");
            String token = jwtUtil.generateToken(email);

            return ResponseEntity.ok(Map.of("token", token, "tipoUsuario", usuario.getTipoUsuario().toString(),  "idUsuario", usuario.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("erro", "Credenciais inválidas"));
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody Map<String, Object> dados){
        UsuarioResponse user = usuarioService.cadastrarUsuario(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
