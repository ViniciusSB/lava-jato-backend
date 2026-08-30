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
                    dados = dashboardRepository.dashboardTextoFuncionarioDia(id, dia, mes, ano);
                    dadosGrafico = dashboardRepository.dashboardGraficoFuncionarioDia(id, dia, mes, ano);
                    break;
                case "mes":
                    if (periodos.length >= 1)
                        mes = Integer.parseInt(periodos[0]);
                    if (periodos.length >= 2)
                        ano = Integer.parseInt(periodos[1]);
                    dados = dashboardRepository.dashboardTextoFuncionarioMes(id, mes, ano);
                    dadosGrafico = dashboardRepository.dashboardGraficoFuncionarioMes(id, mes, ano);
                    break;
                case "ano":
                    ano = periodo.length() == 4 ? Integer.parseInt(periodo) : ano;
                    dados = dashboardRepository.dashboardTextoFuncionarioAno(id, ano);
                    dadosGrafico = dashboardRepository.dashboardGraficoFuncionarioAno(id, ano);
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

        List<Grafico> graficos = dadosGrafico.stream().map(dg -> {
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

        if (tipo.equals("mes")) {
            Map<String, Double> diasAgrupados = graficos.stream()
                    .collect(Collectors.groupingBy(g -> g.getDia(),
                            Collectors.summingDouble(g -> g.getFaturamento())));
            String mesFinal = graficos.get(0).getMes();
            String anoFinal = graficos.get(0).getAno();
            graficos = diasAgrupados.entrySet().stream()
                    .map(entry -> {
                        Grafico g = new Grafico();
                        g.setDia(entry.getKey());
                        g.setFaturamento(entry.getValue());
                        g.setAno(anoFinal);
                        g.setMes(mesFinal);
                        g.setHora("");
                        return g;
                    })
                    .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getDia())))
                    .collect(Collectors.toList());
            System.out.println("");
        }

        else if (tipo.equals("ano")) {
            Map<String, Double> mesesAgrupados = graficos.stream()
                    .collect(Collectors.groupingBy(g -> g.getMes(),
                            Collectors.summingDouble(g -> g.getFaturamento())));
            String anoFinal = graficos.get(0).getAno();
            graficos = mesesAgrupados.entrySet().stream()
                    .map(entry -> {
                        Grafico g = new Grafico();
                        g.setDia("");
                        g.setFaturamento(entry.getValue());
                        g.setAno(anoFinal);
                        g.setMes(entry.getKey());
                        g.setHora("");
                        return g;
                    })
                    .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getDia())))
                    .collect(Collectors.toList());
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
        if (Integer.parseInt(dadosServico.get("atendidos").toString()) == 0)
            atendimento = null;
        else {
            Integer atendidos = Integer.parseInt(dadosServico.get("atendidos").toString());
            Integer finalizados = Integer.parseInt(dadosServico.get("finalizados").toString());
            Integer andamento = Integer.parseInt(dadosServico.get("em_andamento").toString());
            List<DadosVeiculos> veiculos = new ArrayList<>();
            if (dadosVeiculos.size() > 0) {
                veiculos = dadosVeiculos.stream().map(dv -> {
                    String tipoVeiculo = dv.get("tipo").toString();
                    Integer quantidade = Integer.parseInt(dv.get("quantidade").toString());
                    DadosVeiculos dado = new DadosVeiculos(tipoVeiculo, quantidade);
                    return dado;
                }).sorted(Comparator.comparing(v->v.getQuantidade())).collect(Collectors.toList());
            }
            atendimento = new DadosAtendimento(atendidos, finalizados, andamento, veiculos);
        }

        DadosFaturamento faturamento = null;
        if (dadosFaturamento.size() != 0) {
            Double totalBruto = dadosFaturamento.stream().mapToDouble(df -> df.get("valor_bruto") != null ? Double.parseDouble(df.get("valor_bruto").toString()) : 0).sum();
            Double totalLiquido = dadosFaturamento.stream().mapToDouble(df -> df.get("valor_liquido") != null ? Double.parseDouble(df.get("valor_liquido").toString()) : 0).sum();
            List<GraficoFaturamento> graficos = dadosFaturamento.stream().map(df -> {
                GraficoFaturamento grafico = new GraficoFaturamento();
                LocalDateTime dataHora = (LocalDateTime) df.get("data_criacao");
                grafico.setValorBruto(df.get("valor_bruto") == null ? 0 : Double.parseDouble(df.get("valor_bruto").toString()));
                grafico.setValorLiquido(df.get("valor_liquido") == null ? 0 : Double.parseDouble(df.get("valor_liquido").toString()));
                grafico.setAno(String.valueOf(dataHora.getYear()));
                grafico.setMes(String.valueOf(dataHora.getMonthValue()));
                grafico.setDia(String.valueOf(dataHora.getDayOfMonth()));
                grafico.setHora(String.valueOf(dataHora.getHour() + ":" + dataHora.getMinute()));
                return grafico;
            }).collect(Collectors.toList());
            if (tipo.equals("dia")) {
                Map<String, Double> brutoHora = graficos.stream().collect(Collectors.groupingBy(g -> g.getHora(), 
                Collectors.summingDouble(g->g.getValorBruto())));
                Map<String, Double> liquidoHora = graficos.stream().collect(Collectors.groupingBy(g -> g.getHora(), 
                Collectors.summingDouble(g->g.getValorLiquido())));

                List<GraficoFaturamento> dadosGraficoBruto = brutoHora.entrySet().stream().map(gb -> {
                    GraficoFaturamento gf = new GraficoFaturamento();
                    gf.setValorBruto(gb.getValue());
                    gf.setHora(gb.getKey());
                    gf.setDia(graficos.getFirst().getDia());
                    gf.setMes(graficos.getFirst().getMes());
                    gf.setAno(graficos.getFirst().getAno());
                    return gf;
                }).sorted(Comparator.comparing(c->c.getHora())).collect(Collectors.toList());

                List<GraficoFaturamento> dadosGraficoLiquido = liquidoHora.entrySet().stream().map(gl -> {
                    GraficoFaturamento gf = new GraficoFaturamento();
                    gf.setValorLiquido(gl.getValue());
                    gf.setHora(gl.getKey());
                    gf.setDia(graficos.getFirst().getDia());
                    gf.setMes(graficos.getFirst().getMes());
                    gf.setAno(graficos.getFirst().getAno());
                    return gf;
                }).sorted(Comparator.comparing(c->c.getHora())).collect(Collectors.toList());
                faturamento = new DadosFaturamento(totalBruto, totalLiquido, dadosGraficoBruto, dadosGraficoLiquido);
            } else if (tipo.equals("mes")) {
                Map<String, Double> brutoDia = graficos.stream().collect(Collectors.groupingBy(g -> g.getDia(), 
                Collectors.summingDouble(g->g.getValorBruto())));
                Map<String, Double> liquidoDia = graficos.stream().collect(Collectors.groupingBy(g -> g.getDia(), 
                Collectors.summingDouble(g->g.getValorLiquido())));

                List<GraficoFaturamento> dadosGraficoBruto = brutoDia.entrySet().stream().map(gb -> {
                    GraficoFaturamento gf = new GraficoFaturamento();
                    gf.setValorBruto(gb.getValue());
                    gf.setDia(gb.getKey());
                    gf.setMes(graficos.getFirst().getMes());
                    gf.setAno(graficos.getFirst().getAno());
                    return gf;
                }).sorted(Comparator.comparing(c->c.getDia())).collect(Collectors.toList());

                List<GraficoFaturamento> dadosGraficoLiquido = liquidoDia.entrySet().stream().map(gl -> {
                    GraficoFaturamento gf = new GraficoFaturamento();
                    gf.setValorLiquido(gl.getValue());
                    gf.setDia(gl.getKey());
                    gf.setMes(graficos.getFirst().getMes());
                    gf.setAno(graficos.getFirst().getAno());
                    return gf;
                }).sorted(Comparator.comparing(c->c.getDia())).collect(Collectors.toList());
                faturamento = new DadosFaturamento(totalBruto, totalLiquido, dadosGraficoBruto, dadosGraficoLiquido);
            } else {
                Map<String, Double> brutoMes = graficos.stream().collect(Collectors.groupingBy(g -> g.getMes(), 
                Collectors.summingDouble(g->g.getValorBruto())));
                Map<String, Double> liquidoMes = graficos.stream().collect(Collectors.groupingBy(g -> g.getMes(), 
                Collectors.summingDouble(g->g.getValorLiquido())));

                List<GraficoFaturamento> dadosGraficoBruto = brutoMes.entrySet().stream().map(gb -> {
                    GraficoFaturamento gf = new GraficoFaturamento();
                    gf.setValorBruto(gb.getValue());
                    gf.setMes(gb.getKey());
                    gf.setAno(graficos.getFirst().getAno());
                    return gf;
                }).sorted(Comparator.comparing(c->c.getMes())).collect(Collectors.toList());

                List<GraficoFaturamento> dadosGraficoLiquido = liquidoMes.entrySet().stream().map(gl -> {
                    GraficoFaturamento gf = new GraficoFaturamento();
                    gf.setValorLiquido(gl.getValue());
                    gf.setMes(gl.getKey());
                    gf.setAno(graficos.getFirst().getAno());
                    return gf;
                }).sorted(Comparator.comparing(c->c.getMes())).collect(Collectors.toList());
                faturamento = new DadosFaturamento(totalBruto, totalLiquido, dadosGraficoBruto, dadosGraficoLiquido);
            }
            
        }

        DashboardGerenteResponseDTO dto = new DashboardGerenteResponseDTO(dadosGerais, atendimento, faturamento);

        return dto;
    }
}
