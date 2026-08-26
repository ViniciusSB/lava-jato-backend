package com.lavajato.dto.dashboard.funcionario;

import lombok.Data;

@Data
public class DashboardFuncionarioRequest {
    private String tipo; //dia, mes, ano
    private String periodo; // qual dia, mes, ano
}
