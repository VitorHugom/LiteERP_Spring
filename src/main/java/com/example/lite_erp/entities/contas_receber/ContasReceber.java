package com.example.lite_erp.entities.contas_receber;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "contas_receber")
public class ContasReceber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne()
    @JoinColumn(name = "id_cliente")
    Clientes cliente;

    @Column(name = "numero_documento")
    String numeroDocumento;

    Integer parcela;

    @Column(name = "valor_parcela")
    BigDecimal valorParcela;

    @Column(name = "valor_total")
    BigDecimal valorTotal;

    @OneToOne()
    @JoinColumn(name = "id_forma_pagamento")
    FormaPagamento formaPagamento;

    @OneToOne
    @JoinColumn(name = "id_tipo_cobranca")
    TiposCobranca tiposCobranca;

    @Column(name = "data_vencimento")
    LocalDate dataVencimento;

    private String status;
}
