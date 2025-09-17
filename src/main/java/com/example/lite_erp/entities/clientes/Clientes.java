package com.example.lite_erp.entities.clientes;

import com.example.lite_erp.entities.cidades.Cidades;
import com.example.lite_erp.entities.vendedores.Vendedores;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @Column(name = "tipo_pessoa", length = 10, nullable = false)
    @Schema(description = "Tipo de pessoa do cliente", example = "fisica")
    private String tipoPessoa;  // 'fisica' ou 'juridica'

    @Column(length = 11)
    @Schema(description = "CPF do cliente (apenas para pessoa física)", example = "12345678901")
    private String cpf;

    @Column(length = 14)
    @Schema(description = "CNPJ do cliente (apenas para pessoa jurídica)", example = "12345678000195")
    private String cnpj;

    @Column(name = "nome_fantasia", length = 255)
    @Schema(description = "Nome fantasia do cliente", example = "Empresa XYZ Ltda")
    private String nomeFantasia;

    @Column(name = "razao_social", length = 255)
    @Schema(description = "Razão social do cliente", example = "João Silva")
    private String razaoSocial;

    @Column(length = 10)
    private String cep;

    @Column(length = 255)
    private String endereco;

    @Column(length = 255)
    private String complemento;

    @Column(length = 10)
    private String numero;

    @Column(length = 255)
    private String bairro;

    @ManyToOne
    @JoinColumn(name = "cidade_id")
    private Cidades cidade;

    @Column(length = 20)
    private String celular;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String rg;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 255)
    private String email;

    @Column(name = "estado_inscricao_estadual")
    private Boolean estadoInscricaoEstadual;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedores vendedor;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();

    @Column(name = "limite_credito", precision = 10, scale = 2)
    private BigDecimal limiteCredito;
}
