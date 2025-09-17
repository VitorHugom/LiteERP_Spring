package com.example.lite_erp.entities.forma_pagamento;

import com.example.lite_erp.entities.dias_forma_pagamento.DiasFormaPagamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID da forma de pagamento", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Descrição da forma de pagamento", example = "Cartão de Crédito")
    private String descricao;

    @OneToMany(mappedBy = "formaPagamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "Lista de dias para vencimento da forma de pagamento")
    private List<DiasFormaPagamento> diasFormaPagamento;
}
