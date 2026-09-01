package com.lavajato.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioRequest;
import com.lavajato.dto.dashboard.funcionario.DashboardFuncionarioResponse;
import com.lavajato.dto.dashboard.funcionario.Grafico;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteRequestDTO;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO.DadosAtendimento;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO.DadosEquipe;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO.DadosAtendimento.DadosVeiculos;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO.DadosEquipe.FuncionarioDestaque;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO.DadosFaturamento.GraficoFaturamento;
import com.lavajato.dto.dashboard.gerente.DashboardGerenteResponseDTO.DadosFaturamento;
import com.lavajato.repository.DashboardRepository;

@Service
public class DashboardService {

    @Autowired
    DashboardRepository dashboardRepository;

    public DashboardFuncionarioResponse dashboardFuncionario(Long id, DashboardFuncionarioRequest request) {
        String tipo = request.getTipo() != null ? request.getTipo() : "";
        String periodo = request.getPeriodo() != null ? request.getPeriodo() : "";

        List<Map<String, Object>> dados = new ArrayList<>();
        List<Map<String, Object>> dadosGrafico = new ArrayList<>();

        LocalDate localDate = LocalDate.now();
        Integer dia = localDate.getDayOfMonth();
        Integer mes = localDate.getMonthValue();
        Integer ano = localDate.getYear();

        if (!tipo.isEmpty()) {
            String[] periodos = periodo.split("-");
            switch (tipo) {
                case "dia":
                    if (periodos.length >= 1)
                        dia = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        mes = Integer.parseInt(periodos[1]);
                    if (periodos.length == 3)
                        ano = Integer.parseInt(periodos[2]);
                    dados = dashboardRepository.dashboardTextoFuncionario(id, dia, mes, ano);
                    dadosGrafico = dashboardRepository.dashboardGraficoFuncionario(id, dia, mes, ano);
                    break;
                case "mes":
                    if (periodos.length >= 1)
                        mes = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        ano = Integer.parseInt(periodos[1]);
                    dados = dashboardRepository.dashboardTextoFuncionario(id, null, mes, ano);
                    dadosGrafico = dashboardRepository.dashboardGraficoFuncionario(id, null, mes, ano);
                    break;
                case "ano":
                    ano = periodo.length() == 4 ? Integer.parseInt(periodo) : ano;
                    dados = dashboardRepository.dashboardTextoFuncionario(id, null, null, ano);
                    dadosGrafico = dashboardRepository.dashboardGraficoFuncionario(id, null, null, ano);
                    break;
                default:
                    break;
            }
        }

        Long qtdOrdensFinalizadas = dados.stream().filter(qof -> qof.get("status").equals("FINALIZADO")).count();
        Long qtdOrdensEmAndamento = dados.stream().filter(qoe -> qoe.get("status").equals("EM_ANDAMENTO")).count();
        Double faturamentoTotal = dados.stream()
                .mapToDouble(ft -> ft.get("taxa_funcionario") != null
                        ? Double.parseDouble(ft.get("taxa_funcionario").toString())
                        : 0)
                .sum();

        List<Grafico> graficos = faturamentoMapBancoToFaturamentoFuncionario(dadosGrafico);

        if (tipo.equals("mes")) {
            Map<String, Double> diasAgrupados = graficos.stream()
                    .collect(Collectors.groupingBy(g -> g.getDia(),
                            Collectors.summingDouble(g -> g.getFaturamento())));
            graficos = faturamentoMaptoFaturamentoFuncionario(diasAgrupados, tipo, graficos);
        }

        else if (tipo.equals("ano")) {
            Map<String, Double> mesesAgrupados = graficos.stream()
                    .collect(Collectors.groupingBy(g -> g.getMes(),
                            Collectors.summingDouble(g -> g.getFaturamento())));
            graficos = faturamentoMaptoFaturamentoFuncionario(mesesAgrupados, tipo, graficos);
        }

        return new DashboardFuncionarioResponse(faturamentoTotal, qtdOrdensFinalizadas, qtdOrdensEmAndamento, graficos);
    }

    public DashboardGerenteResponseDTO dashboardGerente(DashboardGerenteRequestDTO request) {
        String tipo = request.getTipo() != null ? request.getTipo() : "";
        String periodo = request.getPeriodo() != null ? request.getPeriodo() : "";

        List<Map<String, Object>> dadosFuncionarios = new ArrayList<>();
        List<Map<String, Object>> dadosFaturamento = new ArrayList<>();
        Map<String, Object> dadosServico = new HashMap<>();
        List<Map<String, Object>> dadosVeiculos = new ArrayList<>();

        LocalDate localDate = LocalDate.now();
        Integer dia = localDate.getDayOfMonth();
        Integer mes = localDate.getMonthValue();
        Integer ano = localDate.getYear();

        if (!tipo.isEmpty()) {
            String[] periodos = periodo.split("-");
            switch (tipo) {
                case "dia":
                    if (periodos.length >= 1)
                        dia = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        mes = Integer.parseInt(periodos[1]);
                    if (periodos.length == 3)
                        ano = Integer.parseInt(periodos[2]);
                    dadosFuncionarios = dashboardRepository.gerenteTotalFuncionarios(dia, mes, ano);
                    dadosFaturamento = dashboardRepository.gerenteFaturamento(dia, mes, ano);
                    dadosServico = dashboardRepository.gerenteInformacoesServico(dia, mes, ano);
                    dadosVeiculos = dashboardRepository.gerenteTipoVeiculosFinalizados(dia, mes, ano);
                    break;
                case "mes":
                    if (periodos.length >= 1)
                        mes = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        ano = Integer.parseInt(periodos[1]);
                    dadosFuncionarios = dashboardRepository.gerenteTotalFuncionarios(null, mes, ano);
                    dadosFaturamento = dashboardRepository.gerenteFaturamento(null, mes, ano);
                    dadosServico = dashboardRepository.gerenteInformacoesServico(null, mes, ano);
                    dadosVeiculos = dashboardRepository.gerenteTipoVeiculosFinalizados(null, mes, ano);
                    break;
                case "ano":
                    ano = periodo.length() == 4 ? Integer.parseInt(periodo) : ano;
                    dadosFuncionarios = dashboardRepository.gerenteTotalFuncionarios(null, null, ano);
                    dadosFaturamento = dashboardRepository.gerenteFaturamento(null, null, ano);
                    dadosServico = dashboardRepository.gerenteInformacoesServico(null, null, ano);
                    dadosVeiculos = dashboardRepository.gerenteTipoVeiculosFinalizados(null, null, ano);
                    break;
                default:
                    break;
            }
        }

        Integer qtdAtendidos = Integer.parseInt(dadosFuncionarios.getFirst().get("servicos_finalizados").toString());
        String nomeDestaque = dadosFuncionarios.getFirst().get("nome").toString();
        FuncionarioDestaque destaque = qtdAtendidos == 0 ? null : new FuncionarioDestaque(nomeDestaque, qtdAtendidos);
        Long totalMembros = dadosFuncionarios.stream().count();
        Long totalFuncionarios = dadosFuncionarios.stream()
                .filter(tf -> tf.get("tipo_usuario").toString().equals("FUNCIONARIO")).count();
        Long totalGerentes = dadosFuncionarios.stream()
                .filter(tf -> tf.get("tipo_usuario").toString().equals("GERENTE")).count();
        Long totalAdmin = dadosFuncionarios.stream().filter(tf -> tf.get("tipo_usuario").toString().equals("ADM"))
                .count();

        DadosEquipe dadosGerais = new DadosEquipe(totalMembros, destaque, totalFuncionarios, totalGerentes, totalAdmin);

        DadosAtendimento atendimento;
        
        if (dadosServico.size() == 0)
            atendimento = null;
        else {
            Integer atendidos = Integer.parseInt(dadosServico.get("atendidos") != null ? dadosServico.get("atendidos").toString() : "0");
            Integer finalizados = Integer.parseInt(dadosServico.get("finalizados") != null ? dadosServico.get("finalizados").toString() : "0");
            Integer andamento = Integer.parseInt(dadosServico.get("em_andamento") != null ? dadosServico.get("em_andamento").toString() : "0");
            List<DadosVeiculos> veiculos = new ArrayList<>();
            if (dadosVeiculos.size() > 0) {
                veiculos = veiculosMapBancoToVeiculos(dadosVeiculos);
            }
            atendimento = new DadosAtendimento(atendidos, finalizados, andamento, veiculos);
        }

        DadosFaturamento faturamento = null;
        if (dadosFaturamento.size() != 0) {
            Double totalBruto = dadosFaturamento.stream().mapToDouble(
                    df -> df.get("valor_bruto") != null ? Double.parseDouble(df.get("valor_bruto").toString()) : 0)
                    .sum();
            Double totalLiquido = dadosFaturamento.stream().mapToDouble(
                    df -> df.get("valor_liquido") != null ? Double.parseDouble(df.get("valor_liquido").toString()) : 0)
                    .sum();
            List<GraficoFaturamento> graficos = faturamentoMapBancoToGraficoFaturamento(dadosFaturamento);
            if (tipo.equals("dia")) {
                Map<String, Double> brutoHora = graficos.stream().collect(Collectors.groupingBy(g -> g.getHora(),
                        Collectors.summingDouble(g -> g.getValorBruto())));
                Map<String, Double> liquidoHora = graficos.stream().collect(Collectors.groupingBy(g -> g.getHora(),
                        Collectors.summingDouble(g -> g.getValorLiquido())));

                List<GraficoFaturamento> dadosGraficoBruto = faturamentoMapToFaturamento(brutoHora, "bruto", tipo,
                        graficos);

                List<GraficoFaturamento> dadosGraficoLiquido = faturamentoMapToFaturamento(liquidoHora, "liq", tipo,
                        graficos);

                faturamento = new DadosFaturamento(totalBruto, totalLiquido, dadosGraficoBruto, dadosGraficoLiquido);
            } else if (tipo.equals("mes")) {
                Map<String, Double> brutoDia = graficos.stream().collect(Collectors.groupingBy(g -> g.getDia(),
                        Collectors.summingDouble(g -> g.getValorBruto())));
                Map<String, Double> liquidoDia = graficos.stream().collect(Collectors.groupingBy(g -> g.getDia(),
                        Collectors.summingDouble(g -> g.getValorLiquido())));

                List<GraficoFaturamento> dadosGraficoBruto = faturamentoMapToFaturamento(brutoDia, "bruto", tipo,
                        graficos);

                List<GraficoFaturamento> dadosGraficoLiquido = faturamentoMapToFaturamento(liquidoDia, "liq", tipo,
                        graficos);

                faturamento = new DadosFaturamento(totalBruto, totalLiquido, dadosGraficoBruto, dadosGraficoLiquido);
            } else {
                Map<String, Double> brutoMes = graficos.stream().collect(Collectors.groupingBy(g -> g.getMes(),
                        Collectors.summingDouble(g -> g.getValorBruto())));
                Map<String, Double> liquidoMes = graficos.stream().collect(Collectors.groupingBy(g -> g.getMes(),
                        Collectors.summingDouble(g -> g.getValorLiquido())));

                List<GraficoFaturamento> dadosGraficoBruto = faturamentoMapToFaturamento(brutoMes, "bruto", "ano",
                        graficos);

                List<GraficoFaturamento> dadosGraficoLiquido = faturamentoMapToFaturamento(liquidoMes, "liq", "ano",
                        graficos);

                faturamento = new DadosFaturamento(totalBruto, totalLiquido, dadosGraficoBruto, dadosGraficoLiquido);
            }

        }

        DashboardGerenteResponseDTO dto = new DashboardGerenteResponseDTO(dadosGerais, atendimento, faturamento);

        return dto;
    }

    /* FUNCIONARIO */

    public static List<Grafico> faturamentoMapBancoToFaturamentoFuncionario(List<Map<String, Object>> dadosGrafico) {
        return dadosGrafico.stream().map(dg -> {
            Grafico grafico = new Grafico();
            grafico.setFaturamento(
                    dg.get("taxa_funcionario") != null ? Double.parseDouble(dg.get("taxa_funcionario").toString()) : 0);

            LocalDateTime dataHora = (LocalDateTime) dg.get("data_criacao");
            grafico.setAno(String.valueOf(dataHora.getYear()));
            grafico.setMes(String.valueOf(dataHora.getMonthValue()));
            grafico.setDia(String.valueOf(dataHora.getDayOfMonth()));
            grafico.setHora(String.valueOf(dataHora.getHour()) + ":" + String.valueOf(dataHora.getMinute()));
            return grafico;
        }).collect(Collectors.toList());
    }

    public static List<Grafico> faturamentoMaptoFaturamentoFuncionario(Map<String, Double> diasAgrupados, String periodo, List<Grafico> referencia ) {
      return  diasAgrupados.entrySet().stream()
                    .map(entry -> {
                        Grafico g = new Grafico();
                        g.setDia(periodo.equals("mes") ? entry.getKey() : "");
                        g.setFaturamento(entry.getValue());
                        g.setAno(referencia.getFirst().getAno());
                        g.setMes(periodo.equals("ano") ? entry.getKey() : referencia.getFirst().getMes());
                        g.setHora(periodo.equals("dia") ? entry.getKey() : "");
                        return g;
                    })
                    .sorted(Comparator.comparing(periodo.equals("mes") ? e -> Integer.parseInt(e.getDia()) : e -> Integer.parseInt(e.getMes())))
                    .collect(Collectors.toList());
    }


    /* GERENTE */
    public static List<DadosVeiculos> veiculosMapBancoToVeiculos(List<Map<String, Object>> dadosVeiculos) {
        return dadosVeiculos.stream().map(dv -> {
                    String tipoVeiculo = dv.get("tipo").toString();
                    Integer quantidade = Integer.parseInt(dv.get("quantidade").toString());
                    DadosVeiculos dado = new DadosVeiculos(tipoVeiculo, quantidade);
                    return dado;
                }).sorted(Comparator.comparing(v -> v.getQuantidade())).collect(Collectors.toList());
    }

    //Preenchendo a lista inicial de grafico faturamento com os dados do banco
    public static List<GraficoFaturamento> faturamentoMapBancoToGraficoFaturamento(
            List<Map<String, Object>> dadosFaturamento) {
        return dadosFaturamento.stream().map(df -> {
            GraficoFaturamento grafico = new GraficoFaturamento();
            LocalDateTime dataHora = (LocalDateTime) df.get("data_criacao");
            grafico.setValorBruto(
                    df.get("valor_bruto") == null ? 0 : Double.parseDouble(df.get("valor_bruto").toString()));
            grafico.setValorLiquido(
                    df.get("valor_liquido") == null ? 0 : Double.parseDouble(df.get("valor_liquido").toString()));
            grafico.setAno(String.valueOf(dataHora.getYear()));
            grafico.setMes(String.valueOf(dataHora.getMonthValue()));
            grafico.setDia(String.valueOf(dataHora.getDayOfMonth()));
            grafico.setHora(String.valueOf(dataHora.getHour() + ":" + dataHora.getMinute()));
            return grafico;
        }).collect(Collectors.toList());
    }

    // Ordena e lista os graficos do Faturamento líquido/bruto
    public static List<GraficoFaturamento> faturamentoMapToFaturamento(Map<String, Double> dados,
            String tipoGrafico, String periodo, List<GraficoFaturamento> referencia) {
        var retorno = dados.entrySet().stream().map(g -> {
            GraficoFaturamento gf = new GraficoFaturamento();
            gf.setValorLiquido(tipoGrafico.equals("liq") ? g.getValue() : null);
            gf.setValorBruto(tipoGrafico.equals("bruto") ? g.getValue() : null);
            gf.setHora(periodo.equals("dia") ? g.getKey() : null);
            gf.setDia(periodo.equals("mes") ? g.getKey() : referencia.getFirst().getDia());
            gf.setMes(periodo.equals("ano") ? g.getKey() : referencia.getFirst().getMes());
            gf.setAno(referencia.getFirst().getAno());
            return gf;
        });
        if (periodo.equals("ano")) {
            return retorno.sorted(Comparator.comparing(c -> Integer.parseInt(c.getMes()))).collect(Collectors.toList());
        } else if (periodo.equals("mes")) {
            return retorno.sorted(Comparator.comparing(c -> Integer.parseInt(c.getDia()))).collect(Collectors.toList());
        } else {
            return retorno.sorted(Comparator.comparing(c -> c.getHora())).collect(Collectors.toList());
        }
    }
}
