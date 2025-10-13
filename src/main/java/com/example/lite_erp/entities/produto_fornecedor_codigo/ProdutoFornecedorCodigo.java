package com.example.lite_erp.entities.produto_fornecedor_codigo;

import com.example.lite_erp.entities.fornecedores.Fornecedores;
import com.example.lite_erp.entities.produtos.Produtos;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "produto_fornecedor_codigo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoFornecedorCodigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do vínculo entre produto e código do fornecedor", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    @Schema(description = "Produto vinculado ao código do fornecedor")
    private Produtos produto;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    @Schema(description = "Fornecedor que utiliza este código para o produto")
    private Fornecedores fornecedor;

    @Column(name = "codigo_fornecedor", nullable = false, length = 100)
    @Schema(description = "Código do produto utilizado pelo fornecedor (aparece no XML da NFe)", example = "PROD-FORN-12345")
    private String codigoFornecedor;

    @Column(name = "ativo", nullable = false)
    @Schema(description = "Indica se o vínculo está ativo", example = "true")
    private Boolean ativo = true;

    @Column(name = "data_cadastro")
    @Schema(description = "Data e hora do cadastro do vínculo", example = "2024-01-15T10:30:00")
    private LocalDateTime dataCadastro = LocalDateTime.now();
}

