package com.example.lite_erp.entities.forma_pagamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FormaPagamentoRequestDTO(String descricao, List<Integer> diasFormaPagamento) {
}
