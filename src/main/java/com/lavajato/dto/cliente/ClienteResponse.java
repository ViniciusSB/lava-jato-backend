package com.lavajato.dto.cliente;

import java.util.List;

import com.lavajato.model.Veiculo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class ClienteResponse {
    private Long id;
    private String nome;
    private String celular;
    private Integer fidelidade;
    private List<Veiculo> veiculos;
}
