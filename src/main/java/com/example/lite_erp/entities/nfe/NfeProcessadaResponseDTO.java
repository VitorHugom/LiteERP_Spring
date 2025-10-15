package com.example.lite_erp.entities.nfe;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Resposta completa do processamento do XML da NFe")
public record NfeProcessadaResponseDTO(
        
        @Schema(description = "Dados principais da nota fiscal")
        DadosNfeDTO dadosNfe,
        
        @Schema(description = "Dados do fornecedor extraídos da NFe")
        DadosFornecedorNfeDTO fornecedor,
        
        @Schema(description = "Lista de itens processados da nota fiscal")
        List<ItemNfeProcessadoDTO> itens,
        
        @Schema(description = "Valor total da nota fiscal", example = "15000.00")
        BigDecimal valorTotal,
        
        @Schema(description = "Mensagem informativa sobre o processamento", example = "NFe processada com sucesso. 8 de 10 itens vinculados automaticamente.")
        String mensagem,
        
        @Schema(description = "Quantidade total de itens na nota", example = "10")
        Integer totalItens,
        
        @Schema(description = "Quantidade de itens vinculados automaticamente", example = "8")
        Integer itensVinculados,
        
        @Schema(description = "Quantidade de itens não vinculados que precisam de atenção", example = "2")
        Integer itensNaoVinculados
) {}

