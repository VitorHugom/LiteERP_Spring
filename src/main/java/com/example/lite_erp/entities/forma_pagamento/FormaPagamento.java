package com.example.lite_erp.entities.forma_pagamento;

import com.example.lite_erp.entities.dias_forma_pagamento.DiasFormaPagamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "forma_pagamento")
public class FormaPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String descricao;

    @OneToMany(mappedBy = "formaPagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DiasFormaPagamento> diasFormaPagamento;
}
