package com.example.lite_erp.entities.produto_fornecedor_codigo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProdutoFornecedorCodigoRequestDTO(
        @Schema(description = "ID do produto no sistema", example = "1")
        Long idProduto,

        @Schema(description = "ID do fornecedor", example = "1")
        Integer idFornecedor,

        @Schema(description = "Código do produto utilizado pelo fornecedor", example = "PROD-FORN-12345")
        String codigoFornecedor,

        @Schema(description = "Indica se o vínculo está ativo", example = "true")
        Boolean ativo
) {
}

