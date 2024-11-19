package com.example.lite_erp.entities.movimento_estoque;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentoEstoqueResponseDTO(
        Long id,
        Long idPedido,
        Integer idRecebimentoMercadoria,
        String produto,
        BigDecimal qtd,

        LocalDateTime dataMovimentacao
) {
    public MovimentoEstoqueResponseDTO(MovimentoEstoque movimentoEstoque) {
        this(
                movimentoEstoque.getId(),
                movimentoEstoque.getItemPedido() != null ? movimentoEstoque.getItemPedido().getPedido().getId() : null,
                movimentoEstoque.getItemRecebimentoMercadoria() != null ? movimentoEstoque.getItemRecebimentoMercadoria().getRecebimento().getIdRecebimento() : null,
                movimentoEstoque.getProduto().getDescricao(),
                movimentoEstoque.getQtd(),
                movimentoEstoque.getDataMovimentacao()
        );
    }
}
