package com.lavajato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query("SELECT u FROM Usuario u WHERE u.ativo = true AND tipoUsuario = 'FUNCIONARIO'")
    public List<Usuario> funcionariosAtivos();

    @Query("SELECT u FROM Usuario u WHERE u.ativo = true ORDER BY u.id")
    public List<Usuario> usuariosAtivos();

    @Query("SELECT u FROM Usuario u WHERE u.ativo = false ORDER BY u.id")
    public List<Usuario> usuariosInativos();

    public Usuario findByEmail(String email);

    @Query("SELECT u.ativo FROM Usuario u WHERE u.id = :id")
    public boolean usuarioAtivo(Long id);

    @Modifying
    @Query("UPDATE Usuario u SET u.ativo = false WHERE u.id = :id")
    public void desativarUsuario(Long id);

    @Modifying
    @Query("UPDATE Usuario u SET u.ativo = true WHERE u.id = :id")
    public void ativarUsuario(Long id);
}
