package com.example.lite_erp.entities.clientes;

public record ClientesBuscaResponseDTO(Long id, String razaoSocial, String nomeVendedor) {
    public ClientesBuscaResponseDTO(Long id, String razaoSocial, String nomeVendedor){
        this.id = id;
        this.razaoSocial = razaoSocial;
        this. nomeVendedor = nomeVendedor;
    }

    public ClientesBuscaResponseDTO(Clientes clientes){
        this(
                clientes.getId(),
                clientes.getRazaoSocial(),
                clientes.getVendedor().getNome()
        );
    }
}
