package com.lavajato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lavajato.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByOrderById();

    @Query("SELECT c.ativo FROM Cliente c WHERE c.id = :id")
    public boolean clienteAtivo(Long id);

    @Modifying
    @Query("UPDATE Cliente c SET c.ativo = false WHERE c.id = :id")
    public void desativarCliente(Long id);

    @Modifying
    @Query("UPDATE Veiculo v SET v.ativo = false WHERE v.cliente.id = :clienteId")
    public void desativarVeiculosDoCliente(Long clienteId);

    @Modifying
    @Query("UPDATE Cliente c SET c.ativo = true WHERE c.id = :id")
    public void ativarCliente(Long id);

    @Modifying
    @Query("UPDATE Veiculo v SET v.ativo = true WHERE v.cliente.id = :clienteId")
    public void ativarVeiculosDoCliente(Long clienteId);
}
