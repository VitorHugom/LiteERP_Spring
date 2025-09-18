package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaFiltroDTO;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaResponseDTO;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.services.MovimentacaoCaixaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fluxo-caixa/movimentacoes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovimentacaoCaixaController {

    @Autowired
    private MovimentacaoCaixaService movimentacaoCaixaService;

    @Operation(
            summary = "Listar todas as movimentações",
            description = "Lista todas as movimentações de caixa com paginação, ordenadas por data decrescente.",
            tags = {"Fluxo de Caixa - Movimentações"}
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
    @GetMapping
    public ResponseEntity<Page<MovimentacaoCaixaResponseDTO>> listarTodas(Pageable pageable) {
        Page<MovimentacaoCaixaResponseDTO> movimentacoes = movimentacaoCaixaService.listarTodas(pageable);
        return ResponseEntity.ok(movimentacoes);
    }

    @Operation(
            summary = "Listar movimentações acessíveis ao usuário",
            description = "Lista movimentações de caixa que o usuário logado pode acessar (próprias contas ou todas se for admin).",
            tags = {"Fluxo de Caixa - Movimentações"}
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
    @GetMapping("/acessiveis")
    public ResponseEntity<Page<MovimentacaoCaixaResponseDTO>> listarAcessiveis(
            Authentication authentication, 
            Pageable pageable) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Page<MovimentacaoCaixaResponseDTO> movimentacoes = 
                movimentacaoCaixaService.listarAcessiveisPorUsuario(usuario.getId(), pageable);
        return ResponseEntity.ok(movimentacoes);
    }

    @Operation(
            summary = "Listar movimentações por conta",
            description = "Lista movimentações de uma conta de caixa específica.",
            tags = {"Fluxo de Caixa - Movimentações"}
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
    @GetMapping("/conta/{contaCaixaId}")
    public ResponseEntity<Page<MovimentacaoCaixaResponseDTO>> listarPorConta(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @PathVariable Long contaCaixaId,
            Pageable pageable) {
        Page<MovimentacaoCaixaResponseDTO> movimentacoes = 
                movimentacaoCaixaService.listarPorConta(contaCaixaId, pageable);
        return ResponseEntity.ok(movimentacoes);
    }

    @Operation(
            summary = "Buscar movimentação por ID",
            description = "Busca uma movimentação específica pelo seu ID.",
            tags = {"Fluxo de Caixa - Movimentações"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimentação encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovimentacaoCaixaResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoCaixaResponseDTO> buscarPorId(
            @Parameter(description = "ID da movimentação", example = "1")
            @PathVariable Long id) {
        return movimentacaoCaixaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Filtrar movimentações",
            description = "Busca movimentações aplicando filtros opcionais por conta, tipo, período, valor, etc.",
            tags = {"Fluxo de Caixa - Movimentações"}
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
    @PostMapping("/filtrar")
    public ResponseEntity<Page<MovimentacaoCaixaResponseDTO>> filtrar(
            @RequestBody MovimentacaoCaixaFiltroDTO filtro,
            Pageable pageable) {
        Page<MovimentacaoCaixaResponseDTO> movimentacoes = 
                movimentacaoCaixaService.filtrar(filtro, pageable);
        return ResponseEntity.ok(movimentacoes);
    }

    @Operation(
            summary = "Criar nova movimentação",
            description = "Cria uma nova movimentação manual de caixa.",
            tags = {"Fluxo de Caixa - Movimentações"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimentação criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MovimentacaoCaixaResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<MovimentacaoCaixaResponseDTO> criar(
            @RequestBody MovimentacaoCaixaRequestDTO dto,
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        MovimentacaoCaixaResponseDTO movimentacaoSalva = 
                movimentacaoCaixaService.salvar(dto, usuario.getId());
        return ResponseEntity.ok(movimentacaoSalva);
    }

    @Operation(
            summary = "Cancelar movimentação",
            description = "Cancela uma movimentação existente (altera status para CANCELADO).",
            tags = {"Fluxo de Caixa - Movimentações"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimentação cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(
            @Parameter(description = "ID da movimentação", example = "1")
            @PathVariable Long id) {
        boolean cancelado = movimentacaoCaixaService.cancelar(id);
        return cancelado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
