package com.example.lite_erp.entities.dias_forma_pagamento;

import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dias_forma_pagamento")
public class DiasFormaPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_forma_pagamento", nullable = false)
    @JsonBackReference
    private FormaPagamento formaPagamento;

    @Column(name = "dias_para_vencimento", nullable = false)
    private Integer diasParaVencimento;
}
