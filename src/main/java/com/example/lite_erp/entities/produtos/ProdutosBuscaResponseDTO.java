package com.example.lite_erp.entities.produtos;

import java.math.BigDecimal;

public record ProdutosBuscaResponseDTO(Long id, String descricao, BigDecimal precoVenda) {
    public ProdutosBuscaResponseDTO(Long id, String descricao, BigDecimal precoVenda){
        this.id = id;
        this.descricao = descricao;
        this.precoVenda = precoVenda;
    }
}
