package com.example.lite_erp.entities.grupo_produto;

public record GrupoProdutosResponseDTO(Long id, String nome) {
    public GrupoProdutosResponseDTO(GrupoProdutos grupoProdutos){
        this(grupoProdutos.getId(), grupoProdutos.getNome());
    }
}
