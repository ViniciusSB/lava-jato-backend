package com.lavajato.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.lavajato.dto.relatorio.RelatorioRequest;
import com.lavajato.repository.RelatorioRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    public byte[] gerarRelatorioFaturamento(RelatorioRequest request) throws JRException, IOException {
        InputStream templateStream = new ClassPathResource("relatorios/faturamento.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateStream);

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        
        Integer dia = null;
        Integer mes = null;
        Integer ano = null;

        if ("dia".equals(request.getTipo())) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioFaturamento(dia, mes, ano);
        } else if ("mes".equals(request.getTipo())) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioFaturamento(null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioFaturamento(null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] gerarRelatorioFuncionario(RelatorioRequest request) throws JRException, IOException {
        InputStream templateStream = new ClassPathResource("relatorios/funcionario.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateStream);

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        
        Integer dia = null;
        Integer mes = null;
        Integer ano = null;

        if ("dia".equals(request.getTipo())) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioFuncionario(request.getFuncionarioId(), dia, mes, ano);
        } else if ("mes".equals(request.getTipo())) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioFuncionario(request.getFuncionarioId(), null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioFuncionario(request.getFuncionarioId(), null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] gerarRelatorioClientes(RelatorioRequest request) throws JRException, IOException {
        InputStream templateStream = new ClassPathResource("relatorios/clientes.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateStream);

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        
        Integer dia = null;
        Integer mes = null;
        Integer ano = null;

        if ("dia".equals(request.getTipo())) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioClientes(dia, mes, ano);
        } else if ("mes".equals(request.getTipo())) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioClientes(null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioClientes(null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] gerarRelatorioFuncionarios(RelatorioRequest request) throws JRException, IOException {
        InputStream templateStream = new ClassPathResource("relatorios/funcionarios.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateStream);

        String[] periodos = request.getPeriodo().split("-");
        List<Map<String, Object>> registros;
        
        Integer dia = null;
        Integer mes = null;
        Integer ano = null;

        if ("dia".equals(request.getTipo())) {
            dia = Integer.parseInt(periodos[0]);
            mes = Integer.parseInt(periodos[1]);
            ano = Integer.parseInt(periodos[2]);
            registros = relatorioRepository.relatorioFuncionarios(dia, mes, ano);
        } else if ("mes".equals(request.getTipo())) {
            mes = Integer.parseInt(periodos[0]);
            ano = Integer.parseInt(periodos[1]);
            registros = relatorioRepository.relatorioFuncionarios(null, mes, ano);
        } else {
            ano = Integer.parseInt(periodos[0]);
            registros = relatorioRepository.relatorioFuncionarios(null, null, ano);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}