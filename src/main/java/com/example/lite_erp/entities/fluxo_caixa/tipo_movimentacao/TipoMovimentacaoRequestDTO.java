package com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para criação/atualização de tipo de movimentação")
public record TipoMovimentacaoRequestDTO(
        @Schema(description = "Descrição do tipo de movimentação", example = "Venda à Vista")
        String descricao,

        @Schema(description = "Categoria da movimentação", example = "RECEITA")
        TipoMovimentacao.CategoriaMovimentacao categoria,

        @Schema(description = "Cor em hexadecimal para identificação visual", example = "#28a745")
        String corHex
) {}
