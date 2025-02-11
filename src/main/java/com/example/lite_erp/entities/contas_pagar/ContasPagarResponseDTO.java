package com.example.lite_erp.entities.contas_pagar;

import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasPagarResponseDTO(
        Long id,
        String numeroDocumento,
        Integer parcela,
        BigDecimal valorParcela,
        BigDecimal valorTotal,
        LocalDate dataVencimento,
        Fornecedores fornecedor,
        FormaPagamento formaPagamento,
        TiposCobranca tipoCobranca,
        String status
) {
    public ContasPagarResponseDTO(ContasPagar contasPagar) {
        this(
                contasPagar.getId(),
                contasPagar.getNumeroDocumento(),
                contasPagar.getParcela(),
                contasPagar.getValorParcela(),
                contasPagar.getValorTotal(),
                contasPagar.getDataVencimento(),
                contasPagar.getFornecedor(),
                contasPagar.getFormaPagamento(),
                contasPagar.getTipoCobranca(),
                contasPagar.getStatus()
        );
    }
}
