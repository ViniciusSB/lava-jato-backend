package com.lavajato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Faturamento;

@Repository
public interface FaturamentoRepository extends JpaRepository<Faturamento, Long>{
    
}
