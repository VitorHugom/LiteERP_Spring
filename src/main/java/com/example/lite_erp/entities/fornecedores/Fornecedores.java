package com.example.lite_erp.entities.fornecedores;

import com.example.lite_erp.entities.cidades.Cidades;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "fornecedores")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Fornecedores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_pessoa", length = 10, nullable = false)
    private String tipoPessoa;  // 'fisica' ou 'juridica'

    @Column(length = 11)
    private String cpf;

    @Column(length = 14)
    private String cnpj;

    @Column(name = "nome_fantasia", length = 255)
    private String nomeFantasia;

    @Column(name = "razao_social", length = 255)
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

    @Column(length = 255)
    private String email;

    @Column(name = "estado_inscricao_estadual")
    private Boolean estadoInscricaoEstadual;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();


}
