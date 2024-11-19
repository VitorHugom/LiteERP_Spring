package com.example.lite_erp.entities.movimento_estoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentoEstoqueRequestDTO(Long idItemPedido, Long idItemRecebimentoMercadoria, Long idProduto, BigDecimal qtd, LocalDateTime dataMovimentacao) {
}
