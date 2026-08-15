package com.lavajato.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavajato.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    
}
