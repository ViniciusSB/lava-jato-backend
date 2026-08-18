package com.lavajato.dto.faturamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class FaturamentoResponse {
    private Long id;
    private BigDecimal valorBruto;
    private BigDecimal comissaoFuncionario;
    private BigDecimal valorLiquido;
    private Long funcionarioId;
    private String nomeFuncionario;
    private Long clienteId;
    private String clienteNome;
    private Long veiculoId;
    private String tipoVeiculo;
    private String tipoServico;
    private LocalDate data;
    private LocalTime hora;
}
