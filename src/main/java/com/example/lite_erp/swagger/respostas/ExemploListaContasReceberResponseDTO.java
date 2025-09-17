package com.example.lite_erp.swagger.respostas;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Lista de contas a receber geradas por pedido")
public class ExemploListaContasReceberResponseDTO {

    @Schema(description = "Lista de contas a receber geradas")
    private List<ContaReceberExemplo> contas;

    @Schema(description = "Exemplo de conta a receber gerada por pedido")
    public static class ContaReceberExemplo {
        
        @Schema(description = "ID da conta a receber", example = "1")
        private Integer id;

        @Schema(description = "Dados do cliente da conta a receber")
        private Clientes cliente;

        @Schema(description = "Número do documento da conta a receber", example = "PED-123")
        private String numeroDocumento;

        @Schema(description = "Número da parcela", example = "1")
        private Integer parcela;

        @Schema(description = "Valor da parcela", example = "500.00")
        private BigDecimal valorParcela;

        @Schema(description = "Valor total da conta a receber", example = "1500.00")
        private BigDecimal valorTotal;

        @Schema(description = "Forma de pagamento da conta a receber")
        private FormaPagamento formaPagamento;

        @Schema(description = "Tipo de cobrança da conta a receber")
        private TiposCobranca tipoCobranca;

        @Schema(description = "Data de vencimento da conta a receber", example = "2024-12-31")
        private LocalDate dataVencimento;

        @Schema(description = "Status da conta a receber", example = "aberta")
        private String status;
    }
}
