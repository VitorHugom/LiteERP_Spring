package com.example.lite_erp.entities.grupo_produto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GrupoProdutosRequestDTO (String nome){
}
