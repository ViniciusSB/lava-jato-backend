package com.lavajato.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email; 
    private String tipo;
    private String urlFoto;
    private String status;
    private String mensagem;

    public UsuarioResponse(String mensagem) {
        this.mensagem = mensagem;
    }
}
