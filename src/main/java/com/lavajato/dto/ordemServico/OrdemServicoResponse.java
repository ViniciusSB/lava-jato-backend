package com.lavajato.dto.ordemServico;

import com.lavajato.dto.usuario.UsuarioResponse;
import com.lavajato.model.Servico;
import com.lavajato.model.Veiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdemServicoResponse {
    private Long id;
    private double preco;
    private Servico servico;
    private String status;
    private ClienteOrdemServico cliente;
    private Veiculo veiculo;
    private UsuarioResponse funcionario;
    private String dataInicio;
    private String mensagem;
}



