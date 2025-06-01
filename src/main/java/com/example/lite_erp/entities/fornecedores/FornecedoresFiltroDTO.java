package com.example.lite_erp.entities.fornecedores;

import java.time.LocalDate;

public record FornecedoresFiltroDTO(
        LocalDate dataCadastroInicial,
        LocalDate dataCadastroFinal,
        Integer cidadeId
) { }
