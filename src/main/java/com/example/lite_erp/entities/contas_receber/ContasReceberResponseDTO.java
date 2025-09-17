package com.example.lite_erp.entities.contas_receber;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasReceberResponseDTO(
        @Schema(description = "ID da conta a receber", example = "1")
        Integer id,

        @Schema(description = "Dados do cliente da conta a receber")
        Clientes cliente,

        @Schema(description = "Número do documento da conta a receber", example = "DOC-2024-001")
        String numeroDocumento,

        @Schema(description = "Número da parcela", example = "1")
        Integer parcela,

        @Schema(description = "Valor da parcela", example = "500.75")
        BigDecimal valorParcela,

        @Schema(description = "Valor total da conta a receber", example = "1500.00")
        BigDecimal valorTotal,

        @Schema(description = "Forma de pagamento da conta a receber")
        FormaPagamento formaPagamento,

        @Schema(description = "Tipo de cobrança da conta a receber")
        TiposCobranca tipoCobranca,

        @Schema(description = "Data de vencimento da conta a receber", example = "2024-12-31")
        LocalDate dataVencimento,

        @Schema(description = "Status da conta a receber", example = "em_aberto")
        String status
) {
    public ContasReceberResponseDTO(ContasReceber contasReceber){
        this(
                contasReceber.id,
                contasReceber.cliente,
                contasReceber.numeroDocumento,
                contasReceber.parcela,
                contasReceber.valorParcela,
                contasReceber.valorTotal,
                contasReceber.formaPagamento,
                contasReceber.tiposCobranca,
                contasReceber.dataVencimento,
                contasReceber.getStatus()
        );
    }
}
