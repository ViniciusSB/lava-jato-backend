package com.lavajato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long>{
    
    @Query("SELECT COUNT(os.id) FROM Servico s INNER JOIN OrdemServico os ON os.servico = s WHERE s.id = :servicoId")
    public Long qtdOrdensByServicoId(Long servicoId);
}
