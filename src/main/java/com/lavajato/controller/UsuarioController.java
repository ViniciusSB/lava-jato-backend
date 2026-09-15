package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.usuario.UsuarioRequest;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.model.Usuario;
import com.lavajato.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody Map<String, Object> dados,
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (usuario.getTipoUsuario().toString().equals("ADM")) {
            UsuarioResponse user = usuarioService.cadastrarUsuario(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<UsuarioResponse> listarUsuarios(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.listarUsuarioPorId(id);
        return ResponseEntity.ok().body(usuario);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@RequestBody UsuarioRequest usuario,
            Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getId() == usuario.getId()) {
            UsuarioResponse response = usuarioService.atualizarUsuario(usuario);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<UsuarioResponse> deletarUsuario(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM")) {
            boolean deletado = usuarioService.deletarUsuario(id);
            if (deletado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
