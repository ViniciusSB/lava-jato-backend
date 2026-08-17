package com.lavajato.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    public Servico (String tipo, String detalhes, double precoBase) {
        this.tipo = tipo;
        this.detalhes = detalhes;
        this.precoBase = precoBase;
    }
}
