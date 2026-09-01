package com.lavajato.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class OrdemServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario funcionario;
    
    @ManyToOne
    private Cliente cliente;
    
    @ManyToOne
    private Veiculo veiculo;

    @ManyToOne
    private Servico servico;

    private double preco;

    @Enumerated(EnumType.STRING)
    private Status status;
    
    private String observacao;
    private boolean entregaDomicilio;
    private String enderecoEntrega;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public enum Status {
        AGUARDANDO,
        EM_ANDAMENTO,
        FINALIZADO;
    }
}
