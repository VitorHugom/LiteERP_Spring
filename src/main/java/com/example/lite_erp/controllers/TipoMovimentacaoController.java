package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoResponseDTO;
import com.example.lite_erp.services.TipoMovimentacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fluxo-caixa/tipos-movimentacao")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TipoMovimentacaoController {

    @Autowired
    private TipoMovimentacaoService tipoMovimentacaoService;

    @Operation(
            summary = "Listar todos os tipos de movimentação",
            description = "Lista todos os tipos de movimentação ativos do sistema, ordenados por descrição.",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoMovimentacaoResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<TipoMovimentacaoResponseDTO>> listarTodos() {
        List<TipoMovimentacaoResponseDTO> tipos = tipoMovimentacaoService.listarTodos();
        return ResponseEntity.ok(tipos);
    }

    @Operation(
            summary = "Buscar tipo de movimentação por ID",
            description = "Busca um tipo de movimentação específico pelo seu ID.",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoMovimentacaoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoMovimentacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID do tipo de movimentação", example = "1")
            @PathVariable Long id) {
        return tipoMovimentacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listar tipos por categoria",
            description = "Lista tipos de movimentação filtrados por categoria (RECEITA, DESPESA, TRANSFERENCIA).",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoMovimentacaoResponseDTO.class)
                    )
            )
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<TipoMovimentacaoResponseDTO>> listarPorCategoria(
            @Parameter(description = "Categoria da movimentação", example = "RECEITA")
            @PathVariable TipoMovimentacao.CategoriaMovimentacao categoria) {
        List<TipoMovimentacaoResponseDTO> tipos = tipoMovimentacaoService.listarPorCategoria(categoria);
        return ResponseEntity.ok(tipos);
    }

    @Operation(
            summary = "Criar novo tipo de movimentação",
            description = "Cria um novo tipo de movimentação no sistema.",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoMovimentacaoResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<TipoMovimentacaoResponseDTO> criar(@RequestBody TipoMovimentacaoRequestDTO dto) {
        TipoMovimentacaoResponseDTO tipoSalvo = tipoMovimentacaoService.salvar(dto);
        return ResponseEntity.ok(tipoSalvo);
    }

    @Operation(
            summary = "Atualizar tipo de movimentação",
            description = "Atualiza um tipo de movimentação existente.",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TipoMovimentacaoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoMovimentacaoResponseDTO> atualizar(
            @Parameter(description = "ID do tipo de movimentação", example = "1")
            @PathVariable Long id,
            @RequestBody TipoMovimentacaoRequestDTO dto) {
        return tipoMovimentacaoService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Inativar tipo de movimentação",
            description = "Inativa um tipo de movimentação (não remove do banco).",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(
            @Parameter(description = "ID do tipo de movimentação", example = "1")
            @PathVariable Long id) {
        boolean inativado = tipoMovimentacaoService.inativar(id);
        return inativado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Ativar tipo de movimentação",
            description = "Reativa um tipo de movimentação inativo.",
            tags = {"Fluxo de Caixa - Tipos de Movimentação"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo não encontrado")
    })
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID do tipo de movimentação", example = "1")
            @PathVariable Long id) {
        boolean ativado = tipoMovimentacaoService.ativar(id);
        return ativado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
