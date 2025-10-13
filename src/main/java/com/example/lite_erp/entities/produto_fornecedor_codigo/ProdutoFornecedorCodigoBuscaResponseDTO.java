package com.example.lite_erp.entities.produto_fornecedor_codigo;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProdutoFornecedorCodigoBuscaResponseDTO(
        @Schema(description = "ID do vínculo", example = "1")
        Long id,

        @Schema(description = "ID do produto", example = "1")
        Long idProduto,

        @Schema(description = "Nome/descrição do produto", example = "Notebook Dell Inspiron 15")
        String nomeProduto,

        @Schema(description = "ID do fornecedor", example = "1")
        Integer idFornecedor,

        @Schema(description = "Nome/razão social do fornecedor", example = "Dell Computadores do Brasil Ltda")
        String nomeFornecedor,

        @Schema(description = "Código do produto no fornecedor", example = "PROD-FORN-12345")
        String codigoFornecedor,

        @Schema(description = "Indica se o vínculo está ativo", example = "true")
        Boolean ativo
) {
    public ProdutoFornecedorCodigoBuscaResponseDTO(Long id, Long idProduto, String nomeProduto, 
                                                     Integer idFornecedor, String nomeFornecedor, 
                                                     String codigoFornecedor, Boolean ativo) {
        this.id = id;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.idFornecedor = idFornecedor;
        this.nomeFornecedor = nomeFornecedor;
        this.codigoFornecedor = codigoFornecedor;
        this.ativo = ativo;
    }
}

