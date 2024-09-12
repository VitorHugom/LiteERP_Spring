package com.example.lite_erp.entities.cidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cidades")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cidades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 2)
    private String estado;

    @Column(name = "codigo_ibge", nullable = false, length = 10)
    private String codigoIbge;
}
