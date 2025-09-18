package com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para tipo de movimentação")
public record TipoMovimentacaoResponseDTO(
        @Schema(description = "ID do tipo de movimentação", example = "1")
        Long id,

        @Schema(description = "Descrição do tipo de movimentação", example = "Venda à Vista")
        String descricao,

        @Schema(description = "Categoria da movimentação", example = "RECEITA")
        TipoMovimentacao.CategoriaMovimentacao categoria,

        @Schema(description = "Cor em hexadecimal", example = "#28a745")
        String corHex,

        @Schema(description = "Indica se está ativo", example = "true")
        Boolean ativo,

        @Schema(description = "Data de criação")
        LocalDateTime dataCriacao
) {
    public TipoMovimentacaoResponseDTO(TipoMovimentacao tipo) {
        this(
                tipo.getId(),
                tipo.getDescricao(),
                tipo.getCategoria(),
                tipo.getCorHex(),
                tipo.getAtivo(),
                tipo.getDataCriacao()
        );
    }
}
