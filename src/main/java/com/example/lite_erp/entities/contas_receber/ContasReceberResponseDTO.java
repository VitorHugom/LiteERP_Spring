package com.example.lite_erp.entities.contas_receber;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasReceberResponseDTO(Integer id, Clientes cliente, String numeroDocumento, Integer parcela, BigDecimal valorParcela, BigDecimal valorTotal, FormaPagamento formaPagamento, TiposCobranca tipoCobranca, LocalDate dataVencimento, String status) {
    public ContasReceberResponseDTO(ContasReceber contasReceber){
        this(
                contasReceber.id,
                contasReceber.cliente,
                contasReceber.numeroDocumento,
                contasReceber.parcela,
                contasReceber.valorParcela,
                contasReceber.valorTotal,
                contasReceber.formaPagamento,
                contasReceber.tiposCobranca,
                contasReceber.dataVencimento,
                contasReceber.getStatus()
        );
    }
}
