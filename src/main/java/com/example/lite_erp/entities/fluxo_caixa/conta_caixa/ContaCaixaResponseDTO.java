package com.example.lite_erp.entities.fluxo_caixa.conta_caixa;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para conta de caixa")
public record ContaCaixaResponseDTO(
        @Schema(description = "ID da conta de caixa", example = "1")
        Long id,

        @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
        String descricao,

        @Schema(description = "Tipo da conta", example = "CAIXA_FISICO")
        ContaCaixa.TipoConta tipo,

        @Schema(description = "Nome do banco", example = "Banco do Brasil")
        String banco,

        @Schema(description = "Número da agência", example = "1234-5")
        String agencia,

        @Schema(description = "Número da conta", example = "12345-6")
        String conta,

        @Schema(description = "Saldo atual da conta", example = "1500.50")
        BigDecimal saldoAtual,

        @Schema(description = "ID do usuário responsável", example = "1")
        Long usuarioResponsavelId,

        @Schema(description = "Nome do usuário responsável", example = "João Silva")
        String usuarioResponsavelNome,

        @Schema(description = "Indica se a conta está ativa", example = "true")
        Boolean ativo,

        @Schema(description = "Data de criação da conta")
        LocalDateTime dataCriacao
) {
    public ContaCaixaResponseDTO(ContaCaixa conta) {
        this(
                conta.getId(),
                conta.getDescricao(),
                conta.getTipo(),
                conta.getBanco(),
                conta.getAgencia(),
                conta.getConta(),
                conta.getSaldoAtual(),
                conta.getUsuarioResponsavel().getId(),
                conta.getUsuarioResponsavel().getNomeUsuario(),
                conta.getAtivo(),
                conta.getDataCriacao()
        );
    }
}
