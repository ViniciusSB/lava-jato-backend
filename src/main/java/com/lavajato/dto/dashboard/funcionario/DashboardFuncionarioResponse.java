package com.lavajato.dto.dashboard.funcionario;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardFuncionarioResponse {
    Double faturamentoTotal;
    Long ordensFinalizadas;
    Long ordensEmAndamento;
    List<Grafico> grafico;
}


