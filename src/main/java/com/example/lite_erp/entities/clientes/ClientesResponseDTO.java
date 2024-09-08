package com.example.lite_erp.entities.clientes;

import com.example.lite_erp.entities.Cidades;
import com.example.lite_erp.entities.vendedores.Vendedores;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientesResponseDTO(
        Long id,
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
) {
    public ClientesResponseDTO(Clientes clientes) {
        this(
                clientes.getId(),
                clientes.getTipoPessoa(),
                clientes.getCpf(),
                clientes.getCnpj(),
                clientes.getNomeFantasia(),
                clientes.getRazaoSocial(),
                clientes.getCep(),
                clientes.getEndereco(),
                clientes.getComplemento(),
                clientes.getNumero(),
                clientes.getBairro(),
                clientes.getCidade(),
                clientes.getCelular(),
                clientes.getTelefone(),
                clientes.getRg(),
                clientes.getDataNascimento(),
                clientes.getEmail(),
                clientes.getEstadoInscricaoEstadual(),
                clientes.getInscricaoEstadual(),
                clientes.getVendedor(),
                clientes.getObservacao(),
                clientes.getStatus(),
                clientes.getDataCadastro(),
                clientes.getLimiteCredito()
        );
    }
}
