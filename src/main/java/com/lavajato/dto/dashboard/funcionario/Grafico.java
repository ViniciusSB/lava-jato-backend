package com.lavajato.dto.dashboard.funcionario;

import lombok.Data;

@Data
public class Grafico {
    String ano;
    String hora;
    String mes;
    String dia;
    Double faturamento;
}
