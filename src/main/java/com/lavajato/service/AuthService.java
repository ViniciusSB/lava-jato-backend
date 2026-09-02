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

    public boolean validarLogin(Map<String, Object> dados) {
        String email = (String) dados.get("email");
        String senha = (String) dados.get("senha");
        Usuario usuario = usuarioRepository.findByEmail(email);
        boolean validado = SenhaUtil.validarSenha(senha, usuario.getSenha());
        if (validado)
            return true;
        else 
            return false;
    }
}
