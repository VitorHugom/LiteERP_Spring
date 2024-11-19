package com.example.lite_erp.entities.itens_recebimento_mercadorias;

import com.example.lite_erp.entities.produtos.Produtos;

import java.math.BigDecimal;

public record ItensRecebimentoMercadoriasResponseDTO(Long id, Produtos produto, BigDecimal quantidade, BigDecimal valorUnitario) {
    public ItensRecebimentoMercadoriasResponseDTO(ItensRecebimentoMercadorias itensRecebimentoMercadorias){
        this(
                itensRecebimentoMercadorias.getId(),
                itensRecebimentoMercadorias.getProduto(),
                itensRecebimentoMercadorias.getQuantidade(),
                itensRecebimentoMercadorias.getValorUnitario()
        );
    }
}
