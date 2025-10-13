package com.example.lite_erp.entities.produto_fornecedor_codigo;

import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.produtos.Produtos;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ProdutoFornecedorCodigoResponseDTO(
        @Schema(description = "ID do vínculo", example = "1")
        Long id,

        @Schema(description = "Dados completos do produto")
        Produtos produto,

        @Schema(description = "Dados completos do fornecedor")
        Fornecedores fornecedor,

        @Schema(description = "Código do produto utilizado pelo fornecedor", example = "PROD-FORN-12345")
        String codigoFornecedor,

        @Schema(description = "Indica se o vínculo está ativo", example = "true")
        Boolean ativo,

        @Schema(description = "Data e hora do cadastro do vínculo", example = "2024-01-15T10:30:00")
        LocalDateTime dataCadastro
) {
    public ProdutoFornecedorCodigoResponseDTO(ProdutoFornecedorCodigo entity) {
        this(
                entity.getId(),
                entity.getProduto(),
                entity.getFornecedor(),
                entity.getCodigoFornecedor(),
                entity.getAtivo(),
                entity.getDataCadastro()
        );
    }
}

