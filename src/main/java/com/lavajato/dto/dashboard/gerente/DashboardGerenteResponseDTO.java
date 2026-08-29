package com.lavajato.dto.dashboard.gerente;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGerenteResponseDTO {

    private DadosEquipe equipe;
    private DadosAtendimento atendimento;
    private DadosFaturamento faturamento;

    @Data 
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadosFaturamento {
        private Double totalBruto;
        private Double totalLiquido;
        private List<GraficoFaturamento> dadosGraficoBruto;
        private List<GraficoFaturamento> dadosGraficoLiquido;

        @Data
        public static class GraficoFaturamento {
            private Double valorBruto;
            private Double valorLiquido;
            private String hora;
            private String dia;
            private String mes;
            private String ano;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadosAtendimento {
        private Integer clientesAtendidos;
        private Integer servicosFinalizados;
        private Integer ordensEmAndamento;
        private List<DadosVeiculos> veiculos;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DadosVeiculos {
            String tipo;
            Integer quantidade;
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DadosEquipe {
        private Long totalMembros;
        private FuncionarioDestaque funcionarioDestaque;
        private Long qtdFuncionarios;
        private Long qtdGerentes;
        private Long qtdAdministrador;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class FuncionarioDestaque {
            private String nome;
            private Integer servicosConcluidos;
        }
    }


    
}
