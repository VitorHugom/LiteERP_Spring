package com.example.lite_erp.entities.nfe;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Item da NFe processado com informações de vinculação")
public record ItemNfeProcessadoDTO(
        
        @Schema(description = "Número sequencial do item na nota", example = "1")
        Integer numeroItem,
        
        @Schema(description = "Código do produto no fornecedor", example = "PROD-12345")
        String codigoProdutoFornecedor,
        
        @Schema(description = "Descrição do produto conforme NFe", example = "Notebook Dell Inspiron 15 i5 8GB 256GB SSD")
        String descricaoProduto,
        
        @Schema(description = "Código NCM do produto", example = "84713012")
        String ncm,
        
        @Schema(description = "Código EAN/GTIN do produto", example = "7891234567890")
        String ean,
        
        @Schema(description = "Quantidade do produto", example = "2.00")
        BigDecimal quantidade,
        
        @Schema(description = "Unidade de medida", example = "UN")
        String unidade,
        
        @Schema(description = "Valor unitário do produto", example = "2500.00")
        BigDecimal valorUnitario,
        
        @Schema(description = "Valor total do item (quantidade x valor unitário)", example = "5000.00")
        BigDecimal valorTotal,
        
        @Schema(description = "Indica se o produto foi vinculado automaticamente", example = "true")
        Boolean produtoVinculado,
        
        @Schema(description = "ID do produto vinculado no sistema (null se não vinculado)", example = "15")
        Long idProdutoVinculado,
        
        @Schema(description = "Descrição do produto vinculado no sistema (null se não vinculado)", example = "Notebook Dell Inspiron 15")
        String descricaoProdutoVinculado,
        
        @Schema(description = "Lista de sugestões de produtos similares para vinculação manual")
        List<SugestaoProdutoDTO> sugestoes
) {}

