package com.lavajato.dto.dashboard.gerente;

import lombok.Data;

@Data
public class DashboardGerenteRequestDTO {
    private String tipo; //dia, mes, ano
    private String periodo; // qual dia, mes, ano
}
