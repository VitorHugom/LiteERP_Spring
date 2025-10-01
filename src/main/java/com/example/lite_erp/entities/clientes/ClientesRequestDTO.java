package com.example.lite_erp.entities.clientes;

import com.example.lite_erp.entities.cidades.Cidades;
import com.example.lite_erp.entities.vendedores.Vendedores;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientesRequestDTO(
        String tipoPessoa,
        String cpf,
        String cnpj,
        String nomeFantasia,
        String razaoSocial,
        String cep,
        String endereco,
        String complemento,
        String numero,
        String bairro,
        Cidades cidade,
        String celular,
        String telefone,
        String rg,
        LocalDate dataNascimento,
        String email,
        Boolean estadoInscricaoEstadual,
        String inscricaoEstadual,
        Vendedores vendedor,
        String observacao,
        Boolean status,
        LocalDate dataCadastro,
        BigDecimal limiteCredito
) {}
