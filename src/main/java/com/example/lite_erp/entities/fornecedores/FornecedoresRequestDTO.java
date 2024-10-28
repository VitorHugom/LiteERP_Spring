package com.example.lite_erp.entities.fornecedores;

import com.example.lite_erp.entities.cidades.Cidades;

import java.time.LocalDate;

public record FornecedoresRequestDTO(
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
        String email,
        Boolean estadoInscricaoEstadual,
        String inscricaoEstadual,
        String observacao,
        Boolean status,
        LocalDate dataCadastro
) {

}
