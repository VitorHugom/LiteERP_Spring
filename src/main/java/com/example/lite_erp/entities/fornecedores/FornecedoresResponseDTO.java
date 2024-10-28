package com.example.lite_erp.entities.fornecedores;

import com.example.lite_erp.entities.cidades.Cidades;

import java.time.LocalDate;

public record FornecedoresResponseDTO(
        Integer id,
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
    public FornecedoresResponseDTO(Fornecedores fornecedores){
        this(
                fornecedores.getId(),
                fornecedores.getTipoPessoa(),
                fornecedores.getCpf(),
                fornecedores.getCnpj(),
                fornecedores.getNomeFantasia(),
                fornecedores.getRazaoSocial(),
                fornecedores.getCep(),
                fornecedores.getEndereco(),
                fornecedores.getComplemento(),
                fornecedores.getNumero(),
                fornecedores.getBairro(),
                fornecedores.getCidade(),
                fornecedores.getCelular(),
                fornecedores.getTelefone(),
                fornecedores.getEmail(),
                fornecedores.getEstadoInscricaoEstadual(),
                fornecedores.getInscricaoEstadual(),
                fornecedores.getObservacao(),
                fornecedores.getStatus(),
                fornecedores.getDataCadastro()
        );
    }
}
