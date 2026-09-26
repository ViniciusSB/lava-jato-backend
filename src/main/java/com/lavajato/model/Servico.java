package com.lavajato.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo; // Lavagem, Lavagem com Polimento
    private String detalhes;
    private double precoBase;
    private boolean ativo;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Servico (String tipo, String detalhes, double precoBase, boolean ativo) {
        this.tipo = tipo;
        this.detalhes = detalhes;
        this.precoBase = precoBase;
        this.ativo = ativo;
    }

    public String obterStatus() {
        return this.ativo ? "ativo" : "inativo";
    }
}
