package com.lavajato.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class UsuarioLoginResponse {
    private Long idUsuario;
    private String nome;
    private String email;
    private String token;
    private String tipoUsuario;
    private String urlFoto;
    private String mensagem;
}
