package com.example.lite_erp.swagger.respostas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Exemplo de resposta completa do processamento do XML da NFe")
public class ExemploNfeProcessadaResponseDTO {

    @Schema(description = "Dados principais da nota fiscal")
    private DadosNfeExemplo dadosNfe;

    @Schema(description = "Dados do fornecedor extraídos da NFe")
    private DadosFornecedorExemplo fornecedor;

    @Schema(description = "Lista de itens processados da nota fiscal")
    private List<ItemNfeProcessadoExemplo> itens;

    @Schema(description = "Valor total da nota fiscal", example = "15000.00")
    private BigDecimal valorTotal;

    @Schema(description = "Mensagem informativa sobre o processamento", 
            example = "NFe processada com sucesso. 8 de 10 itens vinculados automaticamente. 2 item(ns) precisa(m) de vinculação manual.")
    private String mensagem;

    @Schema(description = "Quantidade total de itens na nota", example = "10")
    private Integer totalItens;

    @Schema(description = "Quantidade de itens vinculados automaticamente", example = "8")
    private Integer itensVinculados;

    @Schema(description = "Quantidade de itens não vinculados que precisam de atenção", example = "2")
    private Integer itensNaoVinculados;

    @Schema(description = "Dados principais da NFe")
    public static class DadosNfeExemplo {
        @Schema(description = "Chave de acesso da NFe (44 dígitos)", example = "35230812345678000190550010000123451234567890")
        private String chaveAcesso;

        @Schema(description = "Número da nota fiscal", example = "12345")
        private String numeroNota;

        @Schema(description = "Série da nota fiscal", example = "1")
        private String serie;

        @Schema(description = "Data de emissão da nota fiscal", example = "2024-01-15")
        private LocalDate dataEmissao;

        @Schema(description = "Valor total da nota fiscal", example = "15000.00")
        private BigDecimal valorTotal;
    }

    @Schema(description = "Dados do fornecedor extraídos da NFe")
    public static class DadosFornecedorExemplo {
        @Schema(description = "CNPJ do fornecedor", example = "12.345.678/0001-90")
        private String cnpj;

        @Schema(description = "Razão social do fornecedor", example = "Distribuidora de Informática LTDA")
        private String razaoSocial;

        @Schema(description = "Nome fantasia do fornecedor", example = "TechDistribuidora")
        private String nomeFantasia;

        @Schema(description = "ID do fornecedor no sistema (null se não encontrado)", example = "5")
        private Integer idFornecedor;

        @Schema(description = "Indica se o fornecedor foi encontrado no sistema", example = "true")
        private Boolean encontrado;
    }

    @Schema(description = "Item da NFe processado com informações de vinculação")
    public static class ItemNfeProcessadoExemplo {
        @Schema(description = "Número sequencial do item na nota", example = "1")
        private Integer numeroItem;

        @Schema(description = "Código do produto no fornecedor", example = "DELL-INS15-I5")
        private String codigoProdutoFornecedor;

        @Schema(description = "Descrição do produto conforme NFe", example = "Notebook Dell Inspiron 15 i5 8GB 256GB SSD")
        private String descricaoProduto;

        @Schema(description = "Código NCM do produto", example = "84713012")
        private String ncm;

        @Schema(description = "Código EAN/GTIN do produto", example = "7891234567890")
        private String ean;

        @Schema(description = "Quantidade do produto", example = "2.00")
        private BigDecimal quantidade;

        @Schema(description = "Unidade de medida", example = "UN")
        private String unidade;

        @Schema(description = "Valor unitário do produto", example = "2500.00")
        private BigDecimal valorUnitario;

        @Schema(description = "Valor total do item (quantidade x valor unitário)", example = "5000.00")
        private BigDecimal valorTotal;

        @Schema(description = "Indica se o produto foi vinculado automaticamente", example = "true")
        private Boolean produtoVinculado;

        @Schema(description = "ID do produto vinculado no sistema (null se não vinculado)", example = "15")
        private Long idProdutoVinculado;

        @Schema(description = "Descrição do produto vinculado no sistema (null se não vinculado)", 
                example = "Notebook Dell Inspiron 15")
        private String descricaoProdutoVinculado;

        @Schema(description = "Lista de sugestões de produtos similares para vinculação manual")
        private List<SugestaoProdutoExemplo> sugestoes;
    }

    @Schema(description = "Sugestão de produto para vinculação")
    public static class SugestaoProdutoExemplo {
        @Schema(description = "ID do produto", example = "15")
        private Long idProduto;

        @Schema(description = "Descrição do produto", example = "Notebook Dell Inspiron 15")
        private String descricao;

        @Schema(description = "Marca do produto", example = "Dell")
        private String marca;

        @Schema(description = "Código EAN do produto", example = "7891234567890")
        private String codEan;

        @Schema(description = "Código NCM do produto", example = "84713012")
        private String codNcm;

        @Schema(description = "Score de relevância da sugestão (0-100)", example = "100")
        private Integer score;
    }
}

