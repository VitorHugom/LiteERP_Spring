package com.example.lite_erp.entities.produtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutosResponseDTO(Long id, String descricao, Long grupoProdutos, String marca, LocalDate dataUltimaCompra, BigDecimal precoCompra, BigDecimal precoVenda, BigDecimal peso, String codEan, String codNcm, String codCest) {
    public ProdutosResponseDTO (Produtos produtos){
        this(produtos.getId(), produtos.getDescricao(), produtos.getGrupoProdutos(), produtos.getMarca(), produtos.getDataUltimaCompra(), produtos.getPrecoCompra(), produtos.getPrecoVenda(), produtos.getPeso(),produtos.getCodEan(), produtos.getCodNcm(), produtos.getCodCest());
    }
}
