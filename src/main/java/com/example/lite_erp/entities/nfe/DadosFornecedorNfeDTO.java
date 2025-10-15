package com.example.lite_erp.entities.nfe;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do fornecedor extraídos da NFe")
public record DadosFornecedorNfeDTO(
        
        @Schema(description = "CNPJ do fornecedor", example = "12.345.678/0001-90")
        String cnpj,
        
        @Schema(description = "Razão social do fornecedor", example = "Empresa Fornecedora LTDA")
        String razaoSocial,
        
        @Schema(description = "Nome fantasia do fornecedor", example = "Fornecedor XYZ")
        String nomeFantasia,
        
        @Schema(description = "ID do fornecedor no sistema (null se não encontrado)", example = "1")
        Integer idFornecedor,
        
        @Schema(description = "Indica se o fornecedor foi encontrado no sistema", example = "true")
        Boolean encontrado
) {}

