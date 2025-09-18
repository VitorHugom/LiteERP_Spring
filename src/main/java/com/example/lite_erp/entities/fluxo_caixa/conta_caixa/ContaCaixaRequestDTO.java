package com.example.lite_erp.entities.fluxo_caixa.conta_caixa;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO para criação/atualização de conta de caixa")
public record ContaCaixaRequestDTO(
        @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
        String descricao,

        @Schema(description = "Tipo da conta", example = "CAIXA_FISICO")
        ContaCaixa.TipoConta tipo,

        @Schema(description = "Nome do banco (para contas bancárias)", example = "Banco do Brasil")
        String banco,

        @Schema(description = "Número da agência", example = "1234-5")
        String agencia,

        @Schema(description = "Número da conta", example = "12345-6")
        String conta,

        @Schema(description = "Saldo inicial da conta", example = "1000.00")
        BigDecimal saldoInicial,

        @Schema(description = "ID do usuário responsável", example = "1")
        Long usuarioResponsavelId
) {}
