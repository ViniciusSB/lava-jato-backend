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
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelo;
    private String marca;
    private String cor;
    private String placa;

    @Enumerated(EnumType.STRING)
    private tipoVeiculo tipo;

    @ManyToOne
    private Cliente cliente;

    public enum tipoVeiculo {
        MOTO,
        CARRO,
        CAMINHONETE,
        CAMINHAO;
    }
}
