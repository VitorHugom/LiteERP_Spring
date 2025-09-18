package com.example.lite_erp.entities.fluxo_caixa.centro_custo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para centro de custo")
public record CentroCustoResponseDTO(
        @Schema(description = "ID do centro de custo", example = "1")
        Long id,

        @Schema(description = "Descrição do centro de custo", example = "Vendas")
        String descricao,

        @Schema(description = "Código identificador", example = "VENDAS")
        String codigo,

        @Schema(description = "Indica se está ativo", example = "true")
        Boolean ativo,

        @Schema(description = "Data de criação")
        LocalDateTime dataCriacao
) {
    public CentroCustoResponseDTO(CentroCusto centroCusto) {
        this(
                centroCusto.getId(),
                centroCusto.getDescricao(),
                centroCusto.getCodigo(),
                centroCusto.getAtivo(),
                centroCusto.getDataCriacao()
        );
    }
}
