package com.example.lite_erp.entities.forma_pagamento;

import com.example.lite_erp.entities.dias_forma_pagamento.DiasFormaPagamento;

import java.util.List;

public record FormaPagamentoResponseDTO(Long id, String descricao, List<Integer> diasFormaPagamento) {
    public FormaPagamentoResponseDTO(FormaPagamento formaPagamento) {
        this(
                formaPagamento.getId(),
                formaPagamento.getDescricao(),
                formaPagamento.getDiasFormaPagamento().stream().map(DiasFormaPagamento::getDiasParaVencimento).toList()
        );
    }
}
