package com.lavajato.dto.relatorio;

import lombok.Data;

@Data 
public class RelatorioRequest {
    private String tipo; // Dia, Mês, Ano
    private String periodo; // dd-MM-yyyy, mm-yyyy, yyyy
    private Long funcionarioId;
}
