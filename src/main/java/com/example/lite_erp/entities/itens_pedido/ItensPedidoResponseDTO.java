package com.example.lite_erp.entities.itens_pedido;

import java.math.BigDecimal;

public record ItensPedidoResponseDTO(Long id, Long idPedido, Long idProduto, BigDecimal preco, Integer quantidade) {
    public ItensPedidoResponseDTO(ItensPedido itensPedido){
        this(itensPedido.getId(), itensPedido.getIdPedido(), itensPedido.getIdProduto(), itensPedido.getPreco(), itensPedido.getQuantidade());
    };
}
