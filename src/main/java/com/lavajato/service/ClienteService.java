package com.lavajato.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lavajato.dto.MensagemResponse;
import com.lavajato.dto.cliente.ClienteResponse;
import com.lavajato.dto.veiculo.VeiculoResponse;
import com.lavajato.model.Cliente;
import com.lavajato.model.Veiculo;
import com.lavajato.repository.ClienteRepository;
import com.lavajato.repository.VeiculoRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    VeiculoRepository veiculoRepository;
    
    public ClienteResponse cadastrarCliente(Map<String, Object> dados) {
        String nome = (String) dados.get("nome");
        String celular = (String) dados.get("celular");
        Integer fidelidade = (Integer) dados.get("fidelidade");

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCelular(celular);
        cliente.setFidelidade(fidelidade);
        cliente.setAtivo(true);
        cliente = clienteRepository.save(cliente);

        return clienteToClienteResponse(cliente);
    }

    public ClienteResponse atualizarCliente(Map<String, Object> dados) {
        Long id = Long.parseLong(dados.get("id").toString());
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return null;
        }
        
        String nome = (String) dados.get("nome");
        String celular = (String) dados.get("celular");
        Integer fidelidade = (Integer) dados.get("fidelidade");

        if (nome != null) {
            cliente.setNome(nome);
        }
        if (celular != null) {
            cliente.setCelular(celular);
        }
        if (fidelidade != null) {
            cliente.setFidelidade(fidelidade);
        }

        cliente = clienteRepository.save(cliente);
        
        return clienteToClienteResponse(cliente);
    }

    public ClienteResponse buscarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null)
            return null;
        else 
            return clienteToClienteResponse(cliente);
    }

    public List<ClienteResponse> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAllByOrderById();
        return clientes.stream().map(cliente -> {
            return clienteToClienteResponse(cliente);
        }).toList();
    }

    @Transactional 
    public ResponseEntity<MensagemResponse> desativarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        if (cliente != null) {
            if (cliente.isAtivo()) {
                clienteRepository.desativarCliente(clienteId);
                clienteRepository.desativarVeiculosDoCliente(clienteId);
                return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Cliente desativado"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Cliente já estava desativado"));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<MensagemResponse> ativarCliente(Long clienteId) {
        boolean ativo = clienteRepository.clienteAtivo(clienteId);
        if (!ativo) {
            clienteRepository.ativarCliente(clienteId);
            clienteRepository.ativarVeiculosDoCliente(clienteId);
            return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Cliente ativado"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new MensagemResponse("Cliente já estava ativado"));
        }
    }

    private ClienteResponse clienteToClienteResponse(Cliente cliente) {
        List<VeiculoResponse> veiculoResponses = cliente.getVeiculos().stream().map(v -> {
            return veiculoToVeiculoResponse(v);
        }).toList();
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getCelular(), cliente.getFidelidade(), cliente.obterStatus(), veiculoResponses);
    }

    private VeiculoResponse veiculoToVeiculoResponse(Veiculo veiculo) {
        VeiculoResponse vr = new VeiculoResponse();
        vr.setId(veiculo.getId());
        vr.setTipo(veiculo.getTipo().toString());
        vr.setModelo(veiculo.getModelo());
        vr.setMarca(veiculo.getMarca());
        vr.setPlaca(veiculo.getPlaca());
        vr.setCor(veiculo.getCor());
        vr.setClienteId(veiculo.getCliente().getId());
        vr.setClienteNome(veiculo.getCliente().getNome());
        vr.setStatus(veiculo.obterStatus());
        return vr;
    }
}
