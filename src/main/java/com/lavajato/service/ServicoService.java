package com.lavajato.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lavajato.dto.MensagemResponse;
import com.lavajato.dto.servico.ServicoResponse;
import com.lavajato.model.Servico;
import com.lavajato.repository.ServicoRepository;

import jakarta.transaction.Transactional;

@Service
public class ServicoService {

    @Autowired
    ServicoRepository servicoRepository;

    public List<ServicoResponse> listar() {
        return servicoRepository.findAll().stream().map(servico -> {
            return servicoToServicoResponse(servico);
        }).toList();
    }

    public Servico listarPorId(Long id) {
        return servicoRepository.findById(id).orElse(null);
    }

    public ServicoResponse criar(Map<String, Object> dados) {
        String tipo = dados.get("tipo") != null ? dados.get("tipo").toString() : null;
        String detalhes = dados.get("detalhes") != null ? dados.get("detalhes").toString() : null;
        double precoBase = dados.get("precoBase") != null ? Double.parseDouble(dados.get("precoBase").toString()) : null;

        Servico servico = new Servico(tipo, detalhes, precoBase, true);
        servico = servicoRepository.save(servico);
        return servicoToServicoResponse(servico);
    }

    public ServicoResponse atualizar(Map<String, Object> dados) {
        Long id = dados.get("id") != null ? Long.parseLong(dados.get("id").toString()) : null;
        Servico servico = servicoRepository.findById(id).orElse(null);
        if (servico == null) 
            return null;

        String tipo = dados.get("tipo") != null ? dados.get("tipo").toString() : null;
        String detalhes = dados.get("detalhes") != null ? dados.get("detalhes").toString() : null;
        Double precoBase = dados.get("precoBase") != null ? Double.parseDouble(dados.get("precoBase").toString()) : null;

        servico.setTipo(tipo == null ? servico.getTipo() : tipo);
        servico.setPrecoBase(precoBase == null ? servico.getPrecoBase() : precoBase);
        servico.setDetalhes(detalhes == null ? servico.getDetalhes() : detalhes);
        servico = servicoRepository.save(servico);
        return servicoToServicoResponse(servico);
    }
    
    @Transactional 
    public ResponseEntity<MensagemResponse> desativarServico(Long servicoId) {
        Servico servico = servicoRepository.findById(servicoId).orElse(null);
        if (servico != null) {
            if (servico.isAtivo()) {
                servicoRepository.desativarServico(servicoId);
                return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Serviço desativado"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Serviço já estava desativado"));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<MensagemResponse> ativarServico(Long servicoId) {
        boolean ativo = servicoRepository.servicoAtivo(servicoId);
        if (!ativo) {
            servicoRepository.ativarServico(servicoId);
            return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Serviço ativado"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Serviço já estava ativado"));
        }
    }

    private ServicoResponse servicoToServicoResponse(Servico servico) {
        ServicoResponse sr = new ServicoResponse();
        sr.setId(servico.getId());
        sr.setTipo(servico.getTipo());
        sr.setDetalhes(servico.getDetalhes());
        sr.setPrecoBase(servico.getPrecoBase());
        sr.setStatus(servico.obterStatus());
        return sr;
    }
}
