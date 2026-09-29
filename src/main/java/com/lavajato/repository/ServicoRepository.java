package com.lavajato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long>{

    @Query("SELECT s FROM Servico s WHERE s.ativo = true")
    public List<Servico> servicosAtivos(); 
    
    @Query("SELECT COUNT(os.id) FROM Servico s INNER JOIN OrdemServico os ON os.servico = s WHERE s.id = :servicoId")
    public Long qtdOrdensByServicoId(Long servicoId);

    @Query("SELECT s.ativo FROM Servico s WHERE s.id = :id")
    public boolean servicoAtivo(Long id);

    @Modifying
    @Query("UPDATE Servico s SET s.ativo = false WHERE s.id = :id")
    public void desativarServico(Long id);

    @Modifying
    @Query("UPDATE Servico s SET s.ativo = true WHERE s.id = :id")
    public void ativarServico(Long id);
}
