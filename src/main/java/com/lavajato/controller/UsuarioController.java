package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.usuario.UsuarioRequest;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.dto.usuario.UsuarioSenhaRequest;
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
        if (usuario.getTipoUsuario().toString().equals("ADM") || usuario.getTipoUsuario().toString().equals("GERENTE")) {
            return usuarioService.cadastrarUsuario(dados);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/listarAtivos")
    public ResponseEntity<List<UsuarioResponse>> listarUsuariosAtivos() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/listarInativos")
    public ResponseEntity<List<UsuarioResponse>> listarUsuariosInativos() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuariosInativos();
        return ResponseEntity.ok().body(usuarios);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<UsuarioResponse> listarUsuario(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.listarUsuarioPorId(id);
        return ResponseEntity.ok().body(usuario);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@RequestBody UsuarioRequest usuario,
            Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getId() == usuario.getId()) {
            return usuarioService.atualizarUsuario(usuario);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("/atualizarSenha")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@RequestBody UsuarioSenhaRequest request,
            Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getId() == request.getUsuarioId()) {
            UsuarioResponse response = usuarioService.atualizarSenha(request);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping ("/desativar/{id}")
    public ResponseEntity<UsuarioResponse> desativarUsuario(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getTipoUsuario().toString().equals("GERENTE")) {
            return usuarioService.desativarUsuario(id, user);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping ("/desativarPropriaConta/{id}")
    public ResponseEntity<UsuarioResponse> desativarPropriaConta(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (id == user.getId()) {
            return usuarioService.desativarPropriaConta(id);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping ("/ativar/{id}")
    public ResponseEntity<UsuarioResponse> ativarUsuario(@PathVariable Long id, Authentication authentication) {
        Usuario user = (Usuario) authentication.getPrincipal();
        if (user.getTipoUsuario().toString().equals("ADM") || user.getTipoUsuario().toString().equals("GERENTE")) {
            return usuarioService.ativarUsuario(id);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
