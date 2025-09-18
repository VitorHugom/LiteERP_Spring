package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.DashboardFluxoCaixaResponseDTO;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.services.FluxoCaixaDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fluxo-caixa/dashboard")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FluxoCaixaDashboardController {

    @Autowired
    private FluxoCaixaDashboardService dashboardService;

    @Operation(
            summary = "Obter dashboard do fluxo de caixa",
            description = "Retorna um dashboard completo com indicadores financeiros, incluindo saldos atuais, " +
                         "comparativos mensais, últimas movimentações e resumos por categoria para as contas " +
                         "acessíveis ao usuário logado.",
            tags = {"Fluxo de Caixa - Dashboard"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard gerado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DashboardFluxoCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<DashboardFluxoCaixaResponseDTO> obterDashboard(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        DashboardFluxoCaixaResponseDTO dashboard = dashboardService.gerarDashboard(usuario.getId());
        return ResponseEntity.ok(dashboard);
    }
}
