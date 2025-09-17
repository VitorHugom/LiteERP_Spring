package com.example.lite_erp.entities.contas_receber;

import io.swagger.v3.oas.annotations.media.Schema;

public record ContasReceberPorPedidoRequestDTO(
        @Schema(description = "ID do pedido para gerar as contas a receber", example = "1")
        Long idPedido,
        
        @Schema(description = "ID da forma de pagamento para definir o parcelamento", example = "1")
        Long idFormaPagamento,
        
        @Schema(description = "ID do tipo de cobrança", example = "1")
        Long idTipoCobranca
) {
}
