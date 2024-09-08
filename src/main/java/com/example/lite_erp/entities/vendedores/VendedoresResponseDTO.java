package com.example.lite_erp.entities.vendedores;

public record VendedoresResponseDTO(Long id, String nome, String email, String telefone) {
    public VendedoresResponseDTO(Vendedores vendedores){
        this(vendedores.getId(), vendedores.getNome(), vendedores.getEmail(), vendedores.getTelefone());
    }
}
