package com.example.lite_erp.entities.nfe;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sugestão de produto para vinculação")
public record SugestaoProdutoDTO(
        
        @Schema(description = "ID do produto", example = "1")
        Long idProduto,
        
        @Schema(description = "Descrição do produto", example = "Notebook Dell Inspiron 15")
        String descricao,
        
        @Schema(description = "Marca do produto", example = "Dell")
        String marca,
        
        @Schema(description = "Código EAN do produto", example = "7891234567890")
        String codEan,
        
        @Schema(description = "Código NCM do produto", example = "84713012")
        String codNcm,
        
        @Schema(description = "Score de relevância da sugestão (0-100)", example = "85")
        Integer score
) {}

