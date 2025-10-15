package com.example.lite_erp.entities.nfe;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados principais da Nota Fiscal Eletrônica")
public record DadosNfeDTO(
        
        @Schema(description = "Chave de acesso da NFe (44 dígitos)", example = "35230812345678000190550010000123451234567890")
        String chaveAcesso,
        
        @Schema(description = "Número da nota fiscal", example = "12345")
        String numeroNota,
        
        @Schema(description = "Série da nota fiscal", example = "1")
        String serie,
        
        @Schema(description = "Data de emissão da nota fiscal", example = "2024-01-15")
        LocalDate dataEmissao,
        
        @Schema(description = "Valor total da nota fiscal", example = "1500.00")
        BigDecimal valorTotal
) {}

