package com.lavajato.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.model.Cliente;
import com.lavajato.model.OrdemServico;
import com.lavajato.model.Servico;
import com.lavajato.model.Usuario;
import com.lavajato.model.Veiculo;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.OrdemServicoRepository;
import com.lavajato.repository.ServicoRepository;
import com.lavajato.repository.UsuarioRepository;
import com.lavajato.repository.VeiculoRepository;

@Service
public class OrdemServicoService {

    @Autowired
    OrdemServicoRepository ordemServicoRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    VeiculoRepository veiculoRepository;
    @Autowired
    ServicoRepository servicoRepository;

    public OrdemServico gerar(Map<String, Object> dados) {
        OrdemServico ordemServico = new OrdemServico();

        Long funcionarioId = Long.valueOf(dados.get("funcionarioId").toString());
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        Long veiculoId = Long.valueOf(dados.get("veiculoId").toString());
        Long servicoId = Long.valueOf(dados.get("servicoId").toString());

        String observacao = dados.get("observacao") != null ? (String) dados.get("observacao") : null;
        boolean entregaDomicilio = dados.get("entrega") != null ? (boolean) dados.get("entrega") : false;
        String enderecoEntrega = dados.get("endereco") != null ? (String) dados.get("endereco") : null;

        Usuario funcionario = usuarioRepository.findById(funcionarioId).orElse(null);
        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);
        Servico servico = servicoRepository.findById(servicoId).orElse(null);
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        
        ordemServico.setFuncionario(funcionario);
        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setServico(servico);
        ordemServico.setObservacao(observacao);
        ordemServico.setEnderecoEntrega(enderecoEntrega);
        ordemServico.setEntregaDomicilio(entregaDomicilio);
        ordemServico.setStatus(OrdemServico.Status.EM_ANDAMENTO);

        switch (veiculo.getTipo().toString()) {
            case "MOTO":
                ordemServico.setPreco(servico.getPrecoBase());
                break;
            case "CARRO":
                ordemServico.setPreco(servico.getPrecoBase() + 15);
                break;
            case "CAMINHONETE":
                ordemServico.setPreco(servico.getPrecoBase() + 25);
                break;
            case "CAMINHAO":
                ordemServico.setPreco(servico.getPrecoBase() + 40);
                break;
            default:
                break;
        }
        return ordemServicoRepository.save(ordemServico);
    }
    
}
