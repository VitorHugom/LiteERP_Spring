package com.example.lite_erp.entities.fornecedores;

public record FornecedoresBuscaResponseDTO(Integer id, String razaoSocial, String nomeFantasia) {
    public FornecedoresBuscaResponseDTO(Integer id, String razaoSocial, String nomeFantasia){
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
    }
}
