package com.lavajato.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lavajato.dto.ordemServico.ClienteOrdemServico;
import com.lavajato.dto.ordemServico.OrdemServicoResponse;
import com.lavajato.dto.usuario.UsuarioResponse;
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
    @Autowired
    FaturamentoService faturamentoService;

    public OrdemServicoResponse gerar(Map<String, Object> dados) {
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

        if (cliente.getFidelidade() == 10) {
            ordemServico.setPreco(0);
        } else {
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
        }
        
        ordemServico = ordemServicoRepository.save(ordemServico);
        return ordemServicoToResponse(ordemServico);
    }

    public OrdemServicoResponse atualizar(Map<String, Object> dados) {
        Long ordemServicoId = dados.get("ordemServicoId") != null ? Long.parseLong(dados.get("ordemServicoId").toString()) : null;
        OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId).orElse(null);

        Long funcionarioId = Long.valueOf(dados.get("funcionarioId").toString());
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        Long veiculoId = Long.valueOf(dados.get("veiculoId").toString());
        Long servicoId = Long.valueOf(dados.get("servicoId").toString());

        String observacao = dados.get("observacao") != null ? (String) dados.get("observacao") : null;
        boolean entregaDomicilio = dados.get("entrega") != null ? (boolean) dados.get("entrega") : false;
        String enderecoEntrega = dados.get("endereco") != null ? (String) dados.get("endereco") : null;
        String status = dados.get("status") != null ? (String) dados.get("status") : null;

        Usuario funcionario = usuarioRepository.findById(funcionarioId).orElse(null);
        Veiculo veiculo = veiculoRepository.findById(veiculoId).orElse(null);
        Servico servico = servicoRepository.findById(servicoId).orElse(null);
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        
        ordemServico.setFuncionario(funcionario != null ? funcionario : ordemServico.getFuncionario());
        ordemServico.setCliente(cliente != null ? cliente : ordemServico.getCliente());
        ordemServico.setVeiculo(veiculo != null ? veiculo : ordemServico.getVeiculo());
        ordemServico.setServico(servico != null ? servico : ordemServico.getServico());
        ordemServico.setObservacao(observacao);
        ordemServico.setEnderecoEntrega(enderecoEntrega);
        ordemServico.setEntregaDomicilio(entregaDomicilio);
        ordemServico.setStatus(status != null ? OrdemServico.Status.valueOf(status.toUpperCase()) : ordemServico.getStatus());

        if (cliente.getFidelidade() == 10) {
            ordemServico.setPreco(0);
        } else {
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
        }

        if (ordemServico.getStatus() == OrdemServico.Status.FINALIZADO) {
            if (cliente.getFidelidade() == 10) {
                cliente.setFidelidade(0);
                clienteRepository.save(cliente);
            } else {
                cliente.setFidelidade(cliente.getFidelidade() + 1);
                clienteRepository.save(cliente);
            }
            faturamentoService.gerarFaturamento(ordemServico);
        }

        ordemServico = ordemServicoRepository.save(ordemServico);
        
        return ordemServicoToResponse(ordemServico);
    }

    public List<OrdemServicoResponse> listar() {
        List<OrdemServico> ordemServicos = ordemServicoRepository.findAll();
        return ordemServicos.stream().map(os -> {
            OrdemServicoResponse response = ordemServicoToResponse(os);
            return response;
        }).collect(Collectors.toList());
    }

    public OrdemServicoResponse listarPorId(Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id).orElse(null);
        if (ordemServico != null) {
            return ordemServicoToResponse(ordemServico);
        } else {
            return null;
        }
    }

    public boolean deletar(Long id) {
        if (ordemServicoRepository.existsById(id)) {
            ordemServicoRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public OrdemServicoResponse ordemServicoToResponse(OrdemServico ordemServico) {
        OrdemServicoResponse response = new OrdemServicoResponse();
        response.setId(ordemServico.getId());
        response.setFuncionario(new UsuarioResponse(ordemServico.getFuncionario().getId(), ordemServico.getFuncionario().getNome(), ordemServico.getFuncionario().getEmail(), ordemServico.getFuncionario().getTipoUsuario().toString()));
        response.setCliente(new ClienteOrdemServico(ordemServico.getCliente().getId(),ordemServico.getCliente().getNome(), ordemServico.getCliente().getCelular(), ordemServico.getCliente().getFidelidade()));
        response.setVeiculo(ordemServico.getVeiculo());
        response.setServico(ordemServico.getServico());
        response.setPreco(ordemServico.getPreco());
        response.setStatus(ordemServico.getStatus().toString());
        response.setObservacao(ordemServico.getObservacao());
        response.setEntregaDomicilio(ordemServico.isEntregaDomicilio());
        response.setEnderecoEntrega(ordemServico.getEnderecoEntrega());
        return response;
    }
    
}
