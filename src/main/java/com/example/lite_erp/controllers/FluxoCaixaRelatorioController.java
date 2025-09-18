package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.ResumoFluxoCaixaResponseDTO;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.services.FluxoCaixaRelatorioService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fluxo-caixa/relatorios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FluxoCaixaRelatorioController {

    @Autowired
    private FluxoCaixaRelatorioService relatorioService;

    @Operation(
            summary = "Gerar resumo do fluxo de caixa",
            description = "Gera um resumo do fluxo de caixa para uma conta específica em um período determinado, mostrando total de receitas, despesas e saldo.",
            tags = {"Fluxo de Caixa - Relatórios"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumo gerado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResumoFluxoCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/resumo")
    public ResponseEntity<ResumoFluxoCaixaResponseDTO> gerarResumo(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @RequestParam Long contaCaixaId,
            
            @Parameter(description = "Data inicial do período", example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            
            @Parameter(description = "Data final do período", example = "2024-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        ResumoFluxoCaixaResponseDTO resumo = relatorioService.gerarResumoFluxoCaixa(
                contaCaixaId, dataInicio, dataFim);
        return ResponseEntity.ok(resumo);
    }

    @Operation(
            summary = "Gerar resumo consolidado",
            description = "Gera um resumo consolidado do fluxo de caixa para todas as contas acessíveis ao usuário em um período determinado.",
            tags = {"Fluxo de Caixa - Relatórios"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumo consolidado gerado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResumoFluxoCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/resumo-consolidado")
    public ResponseEntity<List<ResumoFluxoCaixaResponseDTO>> gerarResumoConsolidado(
            @Parameter(description = "Data inicial do período", example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            
            @Parameter(description = "Data final do período", example = "2024-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            
            Authentication authentication) {
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<ResumoFluxoCaixaResponseDTO> resumos = relatorioService.gerarResumoConsolidado(
                usuario.getId(), dataInicio, dataFim);
        return ResponseEntity.ok(resumos);
    }

    @Operation(
            summary = "Calcular saldo na data",
            description = "Calcula o saldo de uma conta de caixa em uma data específica, considerando todas as movimentações até aquela data.",
            tags = {"Fluxo de Caixa - Relatórios"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Saldo calculado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)
                    )
            )
    })
    @GetMapping("/saldo-na-data")
    public ResponseEntity<BigDecimal> calcularSaldoNaData(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @RequestParam Long contaCaixaId,
            
            @Parameter(description = "Data para cálculo do saldo", example = "2024-01-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        BigDecimal saldo = relatorioService.calcularSaldoNaData(contaCaixaId, data);
        return ResponseEntity.ok(saldo);
    }

    @Operation(
            summary = "Calcular projeção de fluxo",
            description = "Calcula uma projeção do fluxo de caixa baseado em contas a pagar e receber em aberto até uma data limite.",
            tags = {"Fluxo de Caixa - Relatórios"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Projeção calculada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class)
                    )
            )
    })
    @GetMapping("/projecao")
    public ResponseEntity<BigDecimal> calcularProjecao(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @RequestParam Long contaCaixaId,
            
            @Parameter(description = "Data limite para projeção", example = "2024-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataLimite) {
        
        BigDecimal projecao = relatorioService.calcularProjecaoFluxo(contaCaixaId, dataLimite);
        return ResponseEntity.ok(projecao);
    }

    @Operation(
            summary = "Relatório por centro de custo",
            description = "Gera relatório de movimentações agrupadas por centro de custo em um período.",
            tags = {"Fluxo de Caixa - Relatórios"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Relatório gerado com sucesso"
            )
    })
    @GetMapping("/por-centro-custo")
    public ResponseEntity<List<Object[]>> gerarRelatorioPorCentroCusto(
            @Parameter(description = "Data inicial do período", example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            
            @Parameter(description = "Data final do período", example = "2024-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            
            Authentication authentication) {
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<Object[]> relatorio = relatorioService.gerarRelatorioPorCentroCusto(
                usuario.getId(), dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }

    @Operation(
            summary = "Relatório por tipo de movimentação",
            description = "Gera relatório de movimentações agrupadas por tipo em um período.",
            tags = {"Fluxo de Caixa - Relatórios"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Relatório gerado com sucesso"
            )
    })
    @GetMapping("/por-tipo")
    public ResponseEntity<List<Object[]>> gerarRelatorioPorTipo(
            @Parameter(description = "Data inicial do período", example = "2024-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            
            @Parameter(description = "Data final do período", example = "2024-01-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            
            Authentication authentication) {
        
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<Object[]> relatorio = relatorioService.gerarRelatorioPorTipo(
                usuario.getId(), dataInicio, dataFim);
        return ResponseEntity.ok(relatorio);
    }
}
