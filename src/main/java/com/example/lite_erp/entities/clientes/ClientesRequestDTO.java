package com.example.lite_erp.entities.clientes;

import com.example.lite_erp.entities.Cidades;
import com.example.lite_erp.entities.vendedores.Vendedores;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        LocalDateTime dataCadastro,
        BigDecimal limiteCredito
) {}
