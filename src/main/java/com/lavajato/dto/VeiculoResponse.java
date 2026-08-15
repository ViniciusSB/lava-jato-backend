package com.lavajato.dto;

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
}
