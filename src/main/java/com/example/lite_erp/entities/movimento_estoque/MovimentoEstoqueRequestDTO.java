package com.example.lite_erp.entities.movimento_estoque;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovimentoEstoqueRequestDTO(Long idItemPedido, Long idItemRecebimentoMercadoria, Long idProduto, BigDecimal qtd, LocalDateTime dataMovimentacao) {
}
