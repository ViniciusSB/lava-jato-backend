package com.lavajato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lavajato.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("SELECT v FROM Veiculo v WHERE v.cliente.id = :clienteId")
    List<Veiculo> findByClienteId(Long clienteId);
    
}
