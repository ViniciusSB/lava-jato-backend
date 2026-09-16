package com.lavajato.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lavajato.dto.usuario.UsuarioRequest;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.dto.usuario.UsuarioSenhaRequest;
import com.lavajato.model.Usuario;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.util.SenhaUtil;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioResponse cadastrarUsuario(Map<String, Object> dados) {
        String nome = dados.get("nome") != null ? (String) dados.get("nome") : null;
        String email = dados.get("email") != null ? (String) dados.get("email") : null;
        String senha = dados.get("senha") != null ? (String) dados.get("senha") : null;
        String tipo = dados.get("tipo") != null ? (String) dados.get("tipo") : null;

        senha = senha != null ? SenhaUtil.criptografar(senha) : null;

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(Usuario.tipoUsuario.valueOf(tipo.toUpperCase()));

        usuario = usuarioRepository.save(usuario);

        UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.getTipoUsuario().toString(), usuario.getUrlFoto(), "Cadastro realizado");

        return response;
    }

    public List<UsuarioResponse> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAllByOrderById();
        return usuarios.stream().map(u -> {
            UsuarioResponse response = new UsuarioResponse(u.getId(), u.getNome(),
                    u.getEmail(), u.getTipoUsuario().toString(), u.getUrlFoto(), "");
            return response;
        }).collect(Collectors.toList());
    }

    public UsuarioResponse listarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(),
                    usuario.getEmail(), usuario.getTipoUsuario().toString(), usuario.getUrlFoto(), "");
            return response;
        }
        return null;
    }

    public UsuarioResponse atualizarUsuario(UsuarioRequest request) {
        Usuario banco = usuarioRepository.findById(request.getId()).orElse(null);
        if (banco != null) {
            if (request.getNome() != null && !request.getNome().isBlank())
                banco.setNome(request.getNome());
            if (request.getEmail() != null && !request.getEmail().isBlank())
                banco.setEmail(request.getEmail());
            if (request.getTipo() != null && !request.getTipo().isBlank())
                banco.setTipoUsuario(Usuario.tipoUsuario.valueOf(request.getTipo()));
            if (request.getUrlFoto() != null && !request.getUrlFoto().isBlank()) 
                banco.setUrlFoto(request.getUrlFoto());
            usuarioRepository.save(banco);
            return new UsuarioResponse(banco.getId(), banco.getNome(),
                    banco.getEmail(), banco.getTipoUsuario().toString(), banco.getUrlFoto(), "Dados alterados com sucesso");
        }
        return new UsuarioResponse("Usuário não localizado");
    }

    public UsuarioResponse atualizarSenha(UsuarioSenhaRequest request) {
        Usuario banco = usuarioRepository.findById(request.getUsuarioId()).orElse(null);
        if (banco != null) {
            if (request.getSenhaAtual() != null && request.getNovaSenha() != null) {
                boolean match = SenhaUtil.validarSenha(request.getSenhaAtual(), banco.getSenha());
                if (match)
                    banco.setSenha(SenhaUtil.criptografar(request.getNovaSenha()));
                else 
                    return new UsuarioResponse("Senha atual incorreta");
            }
            usuarioRepository.save(banco);
            return new UsuarioResponse(banco.getId(), banco.getNome(),
                    banco.getEmail(), banco.getTipoUsuario().toString(), banco.getUrlFoto(), "Senha alterada");
        }
        return new UsuarioResponse("Usuário não localizado");
    }

    public boolean deletarUsuario(Long usuarioId) {
        if (usuarioRepository.existsById(usuarioId)) {
            usuarioRepository.deleteById(usuarioId);
            return true;
        } else {
            return false;
        }
    }

}
