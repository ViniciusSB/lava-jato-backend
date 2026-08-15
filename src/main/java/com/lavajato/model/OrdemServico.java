package com.lavajato.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    public enum Status {
        AGUARDANDO,
        EM_ANDAMENTO,
        FINALIZADO;
    }
}
