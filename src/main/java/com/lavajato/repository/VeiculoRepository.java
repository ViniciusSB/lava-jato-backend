package com.lavajato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Veiculo;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("SELECT v FROM Veiculo v WHERE v.cliente.id = :clienteId")
    List<Veiculo> findByClienteId(Long clienteId);

    @Query("SELECT COUNT(os.id) FROM Veiculo v INNER JOIN OrdemServico os ON os.veiculo = v WHERE v.id = :veiculoId")
    Long qtdOrdensPorVeiculoId(Long veiculoId);

    @Query("SELECT v.ativo FROM Veiculo v WHERE v.id = :id")
    public boolean veiculoAtivo(Long id);

    @Query("SELECT c.ativo FROM Veiculo v INNER JOIN Cliente c ON c = v.cliente WHERE v.id = :veiculoId")
    public boolean verificarStatusProprietarioDoVeiculo(Long veiculoId);

    @Modifying
    @Query("UPDATE Veiculo v SET v.ativo = false WHERE v.id = :id")
    public void desativarVeiculo(Long id);

    @Modifying
    @Query("UPDATE Veiculo v SET v.ativo = true WHERE v.id = :id")
    public void ativarVeiculo(Long id);
}
