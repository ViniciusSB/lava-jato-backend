package com.lavajato.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lavajato.dto.usuario.UsuarioLoginResponse;
import com.lavajato.model.Usuario;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.util.JwtUtil;
import com.lavajato.util.SenhaUtil;

@Service
public class AuthService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JwtUtil jwtUtil;

    public ResponseEntity<UsuarioLoginResponse> validarLogin(Map<String, Object> dados) {
        UsuarioLoginResponse ulr = new UsuarioLoginResponse();
        String email = (String) dados.get("email");
        String senha = (String) dados.get("senha");
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            if (!usuario.isAtivo()) {
                ulr.setMensagem("Usuário desativado");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ulr);
            }
            boolean validado = usuario != null ? SenhaUtil.validarSenha(senha, usuario.getSenha()) : false;
            if (validado) {
                String token = jwtUtil.generateToken(email);
                ulr = new UsuarioLoginResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), token,
                        usuario.getTipoUsuario().toString(), usuario.getUrlFoto(), "Login realizado com sucesso!");
                return ResponseEntity.ok(ulr);
            } else {
                ulr.setMensagem("Credenciais inválidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ulr);
            }
        } else {
            ulr.setMensagem("Usuário não cadastrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ulr);
        }
    }
}
