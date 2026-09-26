package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.dto.MensagemResponse;
import com.lavajato.dto.cliente.ClienteResponse;
import com.lavajato.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @PostMapping(value = "/cadastrar")
    public ResponseEntity<ClienteResponse> cadastrarCliente(@RequestBody Map<String, Object> dados) {
        ClienteResponse cliente = clienteService.cadastrarCliente(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping(value = "/atualizar")
    public ResponseEntity<ClienteResponse> atualizarCliente(@RequestBody Map<String, Object> dados) {
        ClienteResponse cliente = clienteService.atualizarCliente(dados);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);  
    }

    @GetMapping(value = "/listar/{id}")
    public ResponseEntity<ClienteResponse> ListarClientePorId(@PathVariable Long id) {
        ClienteResponse cliente = clienteService.buscarCliente(id);
        if (cliente != null) {
            return ResponseEntity.status(HttpStatus.OK).body(cliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<ClienteResponse>> ListarTodos() {
        List<ClienteResponse> clientes = clienteService.listarTodos();
        if (clientes != null) {
            return ResponseEntity.status(HttpStatus.OK).body(clientes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping ("/desativar/{id}")
    public ResponseEntity<MensagemResponse> desativarCliente(@PathVariable Long id) {
        return clienteService.desativarCliente(id);
    }

    @PatchMapping ("/ativar/{id}")
    public ResponseEntity<MensagemResponse> ativarCliente(@PathVariable Long id) {
        return clienteService.ativarCliente(id);
    }


}
