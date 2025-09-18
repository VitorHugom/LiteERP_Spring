package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCustoRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCustoResponseDTO;
import com.example.lite_erp.services.CentroCustoService;
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
@RequestMapping("/fluxo-caixa/centros-custo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CentroCustoController {

    @Autowired
    private CentroCustoService centroCustoService;

    @Operation(
            summary = "Listar todos os centros de custo",
            description = "Lista todos os centros de custo ativos do sistema, ordenados por descrição.",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroCustoResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<CentroCustoResponseDTO>> listarTodos() {
        List<CentroCustoResponseDTO> centros = centroCustoService.listarTodos();
        return ResponseEntity.ok(centros);
    }

    @Operation(
            summary = "Buscar centro de custo por ID",
            description = "Busca um centro de custo específico pelo seu ID.",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Centro encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroCustoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Centro não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CentroCustoResponseDTO> buscarPorId(
            @Parameter(description = "ID do centro de custo", example = "1")
            @PathVariable Long id) {
        return centroCustoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Buscar centro de custo por código",
            description = "Busca um centro de custo específico pelo seu código identificador.",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Centro encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroCustoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Centro não encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CentroCustoResponseDTO> buscarPorCodigo(
            @Parameter(description = "Código do centro de custo", example = "VENDAS")
            @PathVariable String codigo) {
        return centroCustoService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Criar novo centro de custo",
            description = "Cria um novo centro de custo no sistema.",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Centro criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroCustoResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CentroCustoResponseDTO> criar(@RequestBody CentroCustoRequestDTO dto) {
        CentroCustoResponseDTO centroSalvo = centroCustoService.salvar(dto);
        return ResponseEntity.ok(centroSalvo);
    }

    @Operation(
            summary = "Atualizar centro de custo",
            description = "Atualiza um centro de custo existente.",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Centro atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CentroCustoResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Centro não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CentroCustoResponseDTO> atualizar(
            @Parameter(description = "ID do centro de custo", example = "1")
            @PathVariable Long id,
            @RequestBody CentroCustoRequestDTO dto) {
        return centroCustoService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Inativar centro de custo",
            description = "Inativa um centro de custo (não remove do banco).",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Centro inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Centro não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(
            @Parameter(description = "ID do centro de custo", example = "1")
            @PathVariable Long id) {
        boolean inativado = centroCustoService.inativar(id);
        return inativado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Ativar centro de custo",
            description = "Reativa um centro de custo inativo.",
            tags = {"Fluxo de Caixa - Centros de Custo"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Centro ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Centro não encontrado")
    })
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(
            @Parameter(description = "ID do centro de custo", example = "1")
            @PathVariable Long id) {
        boolean ativado = centroCustoService.ativar(id);
        return ativado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
