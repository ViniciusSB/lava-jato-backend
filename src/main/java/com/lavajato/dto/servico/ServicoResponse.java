package com.lavajato.dto.servico;

import lombok.Data;

@Data 
public class ServicoResponse {
    private Long id;
    private String tipo;
    private String detalhes;
    private double precoBase;
    private String status;
}
