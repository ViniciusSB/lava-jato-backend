package com.lavajato.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.model.Cliente;
import com.lavajato.repository.ClienteRepository;

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
    ClienteRepository clienteRepository;

    public byte[] gerarRelatorioClientes() throws JRException {
        JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/relatorios/cliente.jrxml");

        List<Cliente> clientes = clienteRepository.findAllByOrderById();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);
        
        byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
        
        return pdf;
    }

}
