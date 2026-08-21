package com.lavajato.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.model.Servico;
import com.lavajato.repository.ServicoRepository;

@Service
public class ServicoService {

    @Autowired
    ServicoRepository servicoRepository;

    public List<Servico> listar() {
        return servicoRepository.findAll();
    }

    public Servico listarPorId(Long id) {
        return servicoRepository.findById(id).orElse(null);
    }

    public Servico criar(Map<String, Object> dados) {
        String tipo = dados.get("tipo") != null ? dados.get("tipo").toString() : null;
        String detalhes = dados.get("detalhes") != null ? dados.get("detalhes").toString() : null;
        double precoBase = dados.get("precoBase") != null ? Double.parseDouble(dados.get("precoBase").toString()) : null;

        Servico servico = new Servico(tipo, detalhes, precoBase);

        return servicoRepository.save(servico);
    }

    public Servico atualizar(Map<String, Object> dados) {
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
        return servicoRepository.save(servico);
    }

    public boolean deletar (Long id) {
        if (servicoRepository.existsById(id)) {
            servicoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
}
