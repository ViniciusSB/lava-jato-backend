package com.lavajato.dto.usuario;

import lombok.Data;

@Data 
public class UsuarioSenhaRequest {
    private Long usuarioId;
    private String senhaAtual;
    private String novaSenha;
}
