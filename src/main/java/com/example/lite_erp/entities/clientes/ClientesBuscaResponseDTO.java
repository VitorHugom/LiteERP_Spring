package com.example.lite_erp.entities.clientes;

public record ClientesBuscaResponseDTO(Long id, String razaoSocial, String nomeVendedor) {
    public ClientesBuscaResponseDTO(Long id, String razaoSocial, String nomeVendedor){
        this.id = id;
        this.razaoSocial = razaoSocial;
        this. nomeVendedor = nomeVendedor;
    }
}
