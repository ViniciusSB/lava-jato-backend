package com.lavajato.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.relatorio.RelatorioRequest;
import com.lavajato.repository.RelatorioRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RelatorioService {

    @Autowired
    RelatorioRepository relatorioRepository;
    private Integer dia;
    private Integer mes;
    private Integer ano;

    public byte[] gerarRelatorioFuncionario(RelatorioRequest request) throws JRException {
        JasperReport jasperReport = JasperCompileManager
                .compileReport("src/main/resources/relatorios/funcionario.jrxml");

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        if (request.getTipo().equals("dia")) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioFuncionario(request.getFuncionarioId(), dia, mes, ano);
        } else if (request.getTipo().equals("mes")) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioFuncionario(request.getFuncionarioId(), null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioFuncionario(request.getFuncionarioId(), null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdf;
    }

    public byte[] gerarRelatorioFaturamento(RelatorioRequest request) throws JRException {
        JasperReport jasperReport = JasperCompileManager
                .compileReport("src/main/resources/relatorios/faturamento.jrxml");
        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        if (request.getTipo().equals("dia")) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioFaturamento(dia, mes, ano);
        } else if (request.getTipo().equals("mes")) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioFaturamento(null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioFaturamento(null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdf;
    }

    public byte[] gerarRelatorioClientes(RelatorioRequest request) throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/relatorios/clientes.jrxml");

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        if (request.getTipo().equals("dia")) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioClientes(dia, mes, ano);
        } else if (request.getTipo().equals("mes")) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioClientes(null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioClientes(null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdf;
    }

    public byte[] gerarRelatorioFuncionarios(RelatorioRequest request) throws JRException {
        JasperReport jasperReport = JasperCompileManager
                .compileReport("src/main/resources/relatorios/funcionarios.jrxml");

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        if (request.getTipo().equals("dia")) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioFuncionarios(dia, mes, ano);
        } else if (request.getTipo().equals("mes")) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioFuncionarios(null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioFuncionarios(null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdf;
    }

}
