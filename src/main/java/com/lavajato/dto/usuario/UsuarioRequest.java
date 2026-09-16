package com.lavajato.dto.usuario;

import lombok.Data;

@Data
public class UsuarioRequest {
    private Long id;
    private String nome;
    private String email; 
    private String tipo;
    private String urlFoto;
}
