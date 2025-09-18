package com.example.lite_erp.entities.contas_receber;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ContasReceberRelatorioFiltroDTO(
        @Schema(description = "Data inicial do período para filtrar as contas a receber", example = "2024-01-01")
        LocalDate dataInicio,

        @Schema(description = "Data final do período para filtrar as contas a receber", example = "2024-12-31")
        LocalDate dataFim,

        @Schema(description = "Status das contas a incluir no relatório. Se não informado, retorna todas as contas", example = "aberta")
        String status,

        @Schema(description = "ID do cliente para filtrar as contas a receber", example = "1")
        Integer idCliente,

        @Schema(description = "ID da forma de pagamento para filtrar as contas a receber", example = "2")
        Integer idFormaPagamento
) {
}
