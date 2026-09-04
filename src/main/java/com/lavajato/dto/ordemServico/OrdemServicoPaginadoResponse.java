package com.lavajato.dto.ordemServico;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdemServicoPaginadoResponse {
    private Long totalItens;
    private Long totalPaginas;
    private Long pagAtual;
    private List<OrdemServicoResponse> ordemServico;
}
