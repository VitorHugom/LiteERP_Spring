package com.example.lite_erp.entities.fluxo_caixa.centro_custo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para criação/atualização de centro de custo")
public record CentroCustoRequestDTO(
        @Schema(description = "Descrição do centro de custo", example = "Vendas")
        String descricao,

        @Schema(description = "Código identificador do centro de custo", example = "VENDAS")
        String codigo
) {}
