package com.lavajato.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lavajato.dto.faturamento.FaturamentoResponse;
import com.lavajato.model.Faturamento;
import com.lavajato.model.OrdemServico;
import com.lavajato.repository.EstabelecimentoRepository;
import com.lavajato.repository.FaturamentoRepository;

@Service
public class FaturamentoService {

    @Autowired
    FaturamentoRepository faturamentoRepository;

    @Autowired
    EstabelecimentoRepository estabelecimentoRepository;

    
    public void gerarFaturamento(OrdemServico ordemServico) {
        Faturamento faturamento = new Faturamento();
        Integer porcFunc = estabelecimentoRepository.obterPorcentagem(1L);
        double ganhoFunc = (porcFunc * 0.01) * ordemServico.getPreco();
        double totalLiquido = ordemServico.getPreco() - ganhoFunc;

        faturamento.setValorBruto(BigDecimal.valueOf(ordemServico.getPreco()));
        faturamento.setTaxaFuncionario(BigDecimal.valueOf(ganhoFunc));
        faturamento.setValorLiquido(BigDecimal.valueOf(totalLiquido));
        faturamento.setOrdemServico(ordemServico);
        faturamentoRepository.save(faturamento);
    }

    public List<FaturamentoResponse> listarFaturamentos() {
        List<Faturamento> faturamentos = faturamentoRepository.findAll();
        return faturamentos.stream().map(f -> {
            FaturamentoResponse response = futuramentoToFaturamentoResponse(f);
            return response;
        }).collect(Collectors.toList());
    }

    public FaturamentoResponse listarPorId(Long id) {
        Faturamento faturamento = faturamentoRepository.findById(id).orElse(null);
        return futuramentoToFaturamentoResponse(faturamento);
    }

    public FaturamentoResponse futuramentoToFaturamentoResponse(Faturamento faturamento) {
        if (faturamento == null)
            return null;
        FaturamentoResponse response = new FaturamentoResponse();
        response.setId(faturamento.getId());
        response.setValorBruto(faturamento.getValorBruto());
        response.setComissaoFuncionario(faturamento.getTaxaFuncionario());
        response.setValorLiquido(faturamento.getValorLiquido());
        response.setFuncionarioId(faturamento.getOrdemServico().getFuncionario().getId());
        response.setNomeFuncionario(faturamento.getOrdemServico().getFuncionario().getNome());
        response.setClienteId(faturamento.getOrdemServico().getCliente().getId());
        response.setClienteNome(faturamento.getOrdemServico().getCliente().getNome());
        response.setVeiculoId(faturamento.getOrdemServico().getVeiculo().getId());
        response.setTipoVeiculo(faturamento.getOrdemServico().getVeiculo().getTipo().toString());
        response.setTipoServico(faturamento.getOrdemServico().getServico().getTipo().toString());
        response.setData(faturamento.getDataCriacao().toLocalDate());
        response.setHora(faturamento.getDataCriacao().toLocalTime().withNano(0));
        return response;
    }
}
