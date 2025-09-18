package com.example.lite_erp.swagger.respostas;

import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Exemplo de resposta para conta de caixa")
public class ExemploContaCaixaResponseDTO {

    @Schema(description = "ID da conta de caixa", example = "1")
    private Long id;

    @Schema(description = "Descrição da conta de caixa", example = "Caixa Principal")
    private String descricao;

    @Schema(description = "Tipo da conta", example = "CAIXA_FISICO")
    private ContaCaixa.TipoConta tipo;

    @Schema(description = "Nome do banco", example = "Banco do Brasil")
    private String banco;

    @Schema(description = "Número da agência", example = "1234-5")
    private String agencia;

    @Schema(description = "Número da conta", example = "12345-6")
    private String conta;

    @Schema(description = "Saldo atual da conta", example = "1500.50")
    private BigDecimal saldoAtual;

    @Schema(description = "ID do usuário responsável", example = "1")
    private Long usuarioResponsavelId;

    @Schema(description = "Nome do usuário responsável", example = "João Silva")
    private String usuarioResponsavelNome;

    @Schema(description = "Indica se a conta está ativa", example = "true")
    private Boolean ativo;

    @Schema(description = "Data de criação da conta", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCriacao;
}
