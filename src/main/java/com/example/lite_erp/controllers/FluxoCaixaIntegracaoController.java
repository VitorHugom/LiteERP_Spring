package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaResponseDTO;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.services.FluxoCaixaIntegracaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fluxo-caixa/integracao")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FluxoCaixaIntegracaoController {

    @Autowired
    private FluxoCaixaIntegracaoService integracaoService;

    @Operation(
            summary = "Processar pagamento de conta a pagar",
            description = "Processa o pagamento de uma conta a pagar, criando automaticamente a movimentação de saída no caixa e atualizando o status da conta.",
            tags = {"Fluxo de Caixa - Integração"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagamento processado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovimentacaoCaixaResponseDTO.class)
                    )
            )
    })
    @PostMapping("/pagar-conta/{contaPagarId}")
    public ResponseEntity<MovimentacaoCaixaResponseDTO> processarPagamento(
            @Parameter(description = "ID da conta a pagar", example = "1")
            @PathVariable Long contaPagarId,
            
            @Parameter(description = "ID da conta de caixa para débito", example = "1")
            @RequestParam Long contaCaixaId,
            
            @Parameter(description = "Data do pagamento", example = "2024-01-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPagamento,
            
            @Parameter(description = "Observações do pagamento")
            @RequestParam(required = false) String observacoes,
            
            Authentication authentication) {
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        MovimentacaoCaixaResponseDTO movimentacao = integracaoService.processarPagamentoContaPagar(
                contaPagarId, contaCaixaId, usuario.getId(), dataPagamento, observacoes);
        return ResponseEntity.ok(movimentacao);
    }

    @Operation(
            summary = "Processar recebimento de conta a receber",
            description = "Processa o recebimento de uma conta a receber, criando automaticamente a movimentação de entrada no caixa e atualizando o status da conta.",
            tags = {"Fluxo de Caixa - Integração"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recebimento processado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovimentacaoCaixaResponseDTO.class)
                    )
            )
    })
    @PostMapping("/receber-conta/{contaReceberId}")
    public ResponseEntity<MovimentacaoCaixaResponseDTO> processarRecebimento(
            @Parameter(description = "ID da conta a receber", example = "1")
            @PathVariable Integer contaReceberId,
            
            @Parameter(description = "ID da conta de caixa para crédito", example = "1")
            @RequestParam Long contaCaixaId,
            
            @Parameter(description = "Data do recebimento", example = "2024-01-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRecebimento,
            
            @Parameter(description = "Observações do recebimento")
            @RequestParam(required = false) String observacoes,
            
            Authentication authentication) {
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        MovimentacaoCaixaResponseDTO movimentacao = integracaoService.processarRecebimentoContaReceber(
                contaReceberId, contaCaixaId, usuario.getId(), dataRecebimento, observacoes);
        return ResponseEntity.ok(movimentacao);
    }

    @Operation(
            summary = "Cancelar movimentação integrada",
            description = "Cancela uma movimentação que foi criada automaticamente por integração, revertendo também o status da conta a pagar/receber.",
            tags = {"Fluxo de Caixa - Integração"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentação cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada")
    })
    @DeleteMapping("/cancelar-movimentacao/{movimentacaoId}")
    public ResponseEntity<Void> cancelarMovimentacaoIntegrada(
            @Parameter(description = "ID da movimentação", example = "1")
            @PathVariable Long movimentacaoId) {
        
        boolean cancelado = integracaoService.cancelarMovimentacaoIntegrada(movimentacaoId);
        return cancelado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Buscar movimentações de conta a pagar",
            description = "Busca todas as movimentações de caixa relacionadas a uma conta a pagar específica.",
            tags = {"Fluxo de Caixa - Integração"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovimentacaoCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/conta-pagar/{contaPagarId}/movimentacoes")
    public ResponseEntity<List<MovimentacaoCaixaResponseDTO>> buscarMovimentacoesContaPagar(
            @Parameter(description = "ID da conta a pagar", example = "1")
            @PathVariable Long contaPagarId) {
        
        List<MovimentacaoCaixaResponseDTO> movimentacoes = 
                integracaoService.buscarMovimentacoesContaPagar(contaPagarId);
        return ResponseEntity.ok(movimentacoes);
    }

    @Operation(
            summary = "Buscar movimentações de conta a receber",
            description = "Busca todas as movimentações de caixa relacionadas a uma conta a receber específica.",
            tags = {"Fluxo de Caixa - Integração"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovimentacaoCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/conta-receber/{contaReceberId}/movimentacoes")
    public ResponseEntity<List<MovimentacaoCaixaResponseDTO>> buscarMovimentacoesContaReceber(
            @Parameter(description = "ID da conta a receber", example = "1")
            @PathVariable Integer contaReceberId) {
        
        List<MovimentacaoCaixaResponseDTO> movimentacoes = 
                integracaoService.buscarMovimentacoesContaReceber(contaReceberId);
        return ResponseEntity.ok(movimentacoes);
    }
}
