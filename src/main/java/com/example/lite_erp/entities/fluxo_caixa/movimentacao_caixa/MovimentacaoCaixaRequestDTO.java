package com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "DTO para criação de movimentação de caixa")
public record MovimentacaoCaixaRequestDTO(
        @Schema(description = "ID da conta de caixa", example = "1")
        Long contaCaixaId,

        @Schema(description = "ID do tipo de movimentação", example = "1")
        Long tipoMovimentacaoId,

        @Schema(description = "ID do centro de custo", example = "1")
        Long centroCustoId,

        @Schema(description = "Número do documento", example = "DOC-001")
        String numeroDocumento,

        @Schema(description = "Descrição da movimentação", example = "Venda à vista")
        String descricao,

        @Schema(description = "Valor da movimentação (positivo=entrada, negativo=saída)", example = "1500.50")
        BigDecimal valor,

        @Schema(description = "Data da movimentação", example = "2024-01-15")
        LocalDate dataMovimentacao,

        @Schema(description = "Observações adicionais", example = "Pagamento à vista com desconto")
        String observacoes
) {}
