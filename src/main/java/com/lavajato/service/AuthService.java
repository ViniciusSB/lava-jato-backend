package com.lavajato.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.model.Usuario;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.util.SenhaUtil;

@Service
public class AuthService {
    
    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario validarLogin(Map<String, Object> dados) {
        String email = (String) dados.get("email");
        String senha = (String) dados.get("senha");
        Usuario usuario = usuarioRepository.findByEmail(email);
        boolean validado = usuario != null ? SenhaUtil.validarSenha(senha, usuario.getSenha()) : false;
        if (validado)
            return usuario;
        else 
            return null;
    }
}
