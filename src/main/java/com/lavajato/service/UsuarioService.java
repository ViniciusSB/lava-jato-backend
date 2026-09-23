package com.lavajato.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lavajato.dto.usuario.UsuarioRequest;
import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.dto.usuario.UsuarioSenhaRequest;
import com.lavajato.model.Usuario;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.util.SenhaUtil;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public ResponseEntity<UsuarioResponse> cadastrarUsuario(Map<String, Object> dados) {
        String nome = dados.get("nome") != null ? (String) dados.get("nome") : null;
        String email = dados.get("email") != null ? (String) dados.get("email") : null;
        String senha = dados.get("senha") != null ? (String) dados.get("senha") : null;
        String tipo = dados.get("tipo") != null ? (String) dados.get("tipo") : null;

        if (tipo != null && tipo.equalsIgnoreCase("adm")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new UsuarioResponse("Sem privilégios para essa ação"));
        }
        Usuario usuarioExistente = usuarioRepository.findByEmail(email);
        if (usuarioExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new UsuarioResponse("Já existe uma conta com esse e-mail"));
        }

        senha = senha != null ? SenhaUtil.criptografar(senha) : null;

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setTipoUsuario(Usuario.tipoUsuario.valueOf(tipo.toUpperCase()));
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);

        UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.getTipoUsuario().toString(), usuario.getUrlFoto(), usuario.obterStatus(), "Cadastro realizado");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public List<UsuarioResponse> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(u -> {
            UsuarioResponse response = new UsuarioResponse(u.getId(), u.getNome(),
                    u.getEmail(), u.getTipoUsuario().toString(), u.getUrlFoto(), u.obterStatus(), "");
            return response;
        }).collect(Collectors.toList());
    }

    public List<UsuarioResponse> listarUsuariosAtivos() {
        List<Usuario> usuarios = usuarioRepository.usuariosAtivos();
        return usuarios.stream().map(u -> {
            UsuarioResponse response = new UsuarioResponse(u.getId(), u.getNome(),
                    u.getEmail(), u.getTipoUsuario().toString(), u.getUrlFoto(), u.obterStatus(), "");
            return response;
        }).collect(Collectors.toList());
    }

    public List<UsuarioResponse> listarUsuariosInativos() {
        List<Usuario> usuarios = usuarioRepository.usuariosInativos();
        return usuarios.stream().map(u -> {
            UsuarioResponse response = new UsuarioResponse(u.getId(), u.getNome(),
                    u.getEmail(), u.getTipoUsuario().toString(), u.getUrlFoto(), u.obterStatus(), "");
            return response;
        }).collect(Collectors.toList());
    }

    public UsuarioResponse listarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(),
                    usuario.getEmail(), usuario.getTipoUsuario().toString(), usuario.getUrlFoto(), usuario.obterStatus(), "");
            return response;
        }
        return null;
    }

    public ResponseEntity<UsuarioResponse> atualizarUsuario(UsuarioRequest request) {
        Usuario banco = usuarioRepository.findById(request.getId()).orElse(null);
        if (banco != null) {
            if (request.getNome() != null && !request.getNome().isBlank())
                banco.setNome(request.getNome());
            if (request.getEmail() != null && !request.getEmail().isBlank())
                banco.setEmail(request.getEmail());
            if (request.getUrlFoto() != null && !request.getUrlFoto().isBlank())
                banco.setUrlFoto(request.getUrlFoto());
            if (request.getTipo() != null && !request.getTipo().isBlank())
                banco.setTipoUsuario(Usuario.tipoUsuario.valueOf(request.getTipo()));
            usuarioRepository.save(banco);
            UsuarioResponse response = new UsuarioResponse(banco.getId(), banco.getNome(),
                    banco.getEmail(), banco.getTipoUsuario().toString(), banco.getUrlFoto(),
                    banco.obterStatus(), "Dados alterados com sucesso");
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsuarioResponse("Usuário não localizado"));
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
                    banco.getEmail(), banco.getTipoUsuario().toString(), banco.getUrlFoto(), banco.obterStatus(), "Senha alterada");
        }
        return new UsuarioResponse("Usuário não localizado");
    }

    @Transactional
    public ResponseEntity<UsuarioResponse> desativarUsuario(Long usuarioId, Usuario usuarioLogado) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario != null) {
            if (usuario.isAtivo()) {
                if (usuarioLogado.getTipoUsuario().toString().equals("GERENTE")
                        && usuario.getTipoUsuario().toString().equals("ADM")) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new UsuarioResponse("Sem privilégios para essa ação"));
                } else if (usuarioLogado.getId() == usuario.getId()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new UsuarioResponse("Desative a sua conta na tela de opções"));
                }
                usuarioRepository.desativarUsuario(usuarioId);
                return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponse("Usuário desativado"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponse("Usuário já estava desativado"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsuarioResponse("Usuário não encontrado"));
        }
    }

    @Transactional
    public ResponseEntity<UsuarioResponse> desativarPropriaConta(Long usuarioId) {
        usuarioRepository.desativarUsuario(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponse("Usuário desativado"));
    }

    @Transactional
    public ResponseEntity<UsuarioResponse> ativarUsuario(Long usuarioId) {
        boolean ativo = usuarioRepository.usuarioAtivo(usuarioId);
        if (!ativo) {
            usuarioRepository.ativarUsuario(usuarioId);
            return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponse("Usuário ativado"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new UsuarioResponse("Usuário já estava ativado"));
        }
    }

}
