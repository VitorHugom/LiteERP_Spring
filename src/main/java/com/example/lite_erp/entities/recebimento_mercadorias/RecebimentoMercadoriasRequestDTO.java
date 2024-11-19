package com.example.lite_erp.entities.recebimento_mercadorias;

import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadoriasRequestDTO;

import java.time.LocalDate;
import java.util.List;

public record RecebimentoMercadoriasRequestDTO(Integer idFornecedor, Long idTipoCobranca, LocalDate dataRecebimento, List<ItensRecebimentoMercadoriasRequestDTO> itens){
}