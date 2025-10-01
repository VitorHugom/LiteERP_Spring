package com.example.lite_erp.entities.recebimento_mercadorias;

import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadoriasRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RecebimentoMercadoriasRequestDTO(Integer idFornecedor, Long idTipoCobranca, LocalDate dataRecebimento, List<ItensRecebimentoMercadoriasRequestDTO> itens, Long idFormaPagamento){
}
