package com.example.lite_erp.entities.itens_pedido;

import java.math.BigDecimal;

public record ItensPedidoResponseDTO(Long id, Long idPedido, Long idProduto, BigDecimal preco, Integer quantidade) {
    public ItensPedidoResponseDTO(ItensPedido itensPedido){
        this(itensPedido.getId(), itensPedido.getPedido().getId(), itensPedido.getProduto().getId(), itensPedido.getPreco(), itensPedido.getQuantidade());
    };
}
