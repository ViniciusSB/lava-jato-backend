package com.lavajato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavajato.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
}
