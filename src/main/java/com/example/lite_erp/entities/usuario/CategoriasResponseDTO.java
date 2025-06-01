package com.example.lite_erp.entities.usuario;

import com.example.lite_erp.entities.categoria_usuario.CategoriasUsuario;

public record CategoriasResponseDTO(
        Long idCategoria,
        String descricaoCategoria
) {
    public CategoriasResponseDTO(CategoriasUsuario c) {
        this(
                c.getId(),
                c.getNome_categoria()
        );
    }
}