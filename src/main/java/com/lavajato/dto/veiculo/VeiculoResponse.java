package com.lavajato.dto.veiculo;

import lombok.Data;

@Data
public class VeiculoResponse {
    private Long id;
    private String modelo;
    private String marca;
    private String cor;
    private String placa;
    private String tipo;
    private Long clienteId;
    private String clienteNome;
    private String mensagem;
}
