package com.example.lite_erp.entities.contas_pagar;

import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contas_pagar")
public class ContasPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_documento", nullable = false, length = 50)
    private String numeroDocumento;

    @Column(nullable = false)
    private Integer parcela;

    @Column(name = "valor_parcela", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorParcela;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @ManyToOne
    @JoinColumn(name = "id_forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cobranca", nullable = false)
    private TiposCobranca tipoCobranca;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedores fornecedor;

    private String status;
}
