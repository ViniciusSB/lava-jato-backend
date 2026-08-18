package com.lavajato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Estabelecimento;

@Repository
public interface EstabelecimentoRepository extends JpaRepository<Estabelecimento, Long>{
    

    @Query("SELECT e.porcentagemFuncionario FROM Estabelecimento e WHERE e.id = :estabelecimentoId")
    public Integer obterPorcentagem(Long estabelecimentoId);
}
