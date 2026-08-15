package com.lavajato.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavajato.model.Cliente;
import com.lavajato.service.ClienteService;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService clienteService;
    
    @GetMapping(value = "/getCliente")
    public String getCliente() {
        return "Hello World";
    }

    @PostMapping(value = "/cadastrar")
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Map<String, Object> dados) {
        Cliente cliente = clienteService.cadastrarCliente(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping(value = "/atualizar/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Map<String, Object> dados) {
        Cliente cliente = clienteService.atualizarCliente(id, dados);
        return ResponseEntity.status(HttpStatus.OK).body(cliente);  
    }

    @GetMapping(value = "/listar/{id}")
    public ResponseEntity<Cliente> ListarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarCliente(id);
        if (cliente != null) {
            return ResponseEntity.status(HttpStatus.OK).body(cliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<Cliente>> ListarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        if (clientes != null) {
            return ResponseEntity.status(HttpStatus.OK).body(clientes);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping(value = "/deletar/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        boolean deletado = clienteService.deletarCliente(id);
        if (deletado) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
