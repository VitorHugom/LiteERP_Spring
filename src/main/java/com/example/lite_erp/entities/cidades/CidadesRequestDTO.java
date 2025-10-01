package com.example.lite_erp.entities.cidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CidadesRequestDTO (String nome, String estado, String codigoIbge){
}
