package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRequestDTO;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaResponseDTO;
import com.example.lite_erp.entities.fluxo_caixa.SaldoContaResponseDTO;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.services.ContaCaixaService;
import com.example.lite_erp.services.UsuarioFluxoCaixaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fluxo-caixa/contas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContaCaixaController {

    @Autowired
    private ContaCaixaService contaCaixaService;

    @Autowired
    private UsuarioFluxoCaixaService usuarioFluxoCaixaService;

    @Operation(
            summary = "Listar todas as contas de caixa",
            description = "Lista todas as contas de caixa ativas do sistema, ordenadas por descrição.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<ContaCaixaResponseDTO>> listarTodas() {
        List<ContaCaixaResponseDTO> contas = contaCaixaService.listarTodas();
        return ResponseEntity.ok(contas);
    }

    @Operation(
            summary = "Listar contas acessíveis ao usuário",
            description = "Lista contas de caixa que o usuário logado pode acessar (próprias ou todas se for admin).",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/acessiveis")
    public ResponseEntity<List<ContaCaixaResponseDTO>> listarContasAcessiveis(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // Garantir que o usuário tenha uma conta padrão
        usuarioFluxoCaixaService.garantirContaCaixaPadrao(usuario);

        List<ContaCaixaResponseDTO> contas = contaCaixaService.listarContasAcessiveis(usuario.getId());
        return ResponseEntity.ok(contas);
    }

    @Operation(
            summary = "Listar contas por usuário",
            description = "Lista contas de caixa de um usuário específico.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaCaixaResponseDTO>> listarPorUsuario(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long usuarioId) {
        List<ContaCaixaResponseDTO> contas = contaCaixaService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(contas);
    }

    @Operation(
            summary = "Buscar conta de caixa por ID",
            description = "Busca uma conta de caixa específica pelo seu ID.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conta encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContaCaixaResponseDTO> buscarPorId(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @PathVariable Long id) {
        return contaCaixaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Obter saldos das contas",
            description = "Obtém o saldo atual de todas as contas acessíveis ao usuário.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Solicitação bem-sucedida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SaldoContaResponseDTO.class)
                    )
            )
    })
    @GetMapping("/saldos")
    public ResponseEntity<List<SaldoContaResponseDTO>> obterSaldos(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<SaldoContaResponseDTO> saldos = contaCaixaService.obterSaldosContas(usuario.getId());
        return ResponseEntity.ok(saldos);
    }

    @Operation(
            summary = "Criar nova conta de caixa",
            description = "Cria uma nova conta de caixa no sistema.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conta criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ContaCaixaResponseDTO> criar(@RequestBody ContaCaixaRequestDTO dto) {
        ContaCaixaResponseDTO contaSalva = contaCaixaService.salvar(dto);
        return ResponseEntity.ok(contaSalva);
    }

    @Operation(
            summary = "Atualizar conta de caixa",
            description = "Atualiza uma conta de caixa existente.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conta atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContaCaixaResponseDTO> atualizar(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @PathVariable Long id,
            @RequestBody ContaCaixaRequestDTO dto) {
        return contaCaixaService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Atualizar saldo da conta",
            description = "Recalcula e atualiza o saldo atual da conta baseado nas movimentações.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo atualizado com sucesso")
    })
    @PatchMapping("/{id}/atualizar-saldo")
    public ResponseEntity<Void> atualizarSaldo(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @PathVariable Long id) {
        contaCaixaService.atualizarSaldo(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Inativar conta de caixa",
            description = "Inativa uma conta de caixa (não remove do banco).",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta inativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(
            @Parameter(description = "ID da conta de caixa", example = "1")
            @PathVariable Long id) {
        boolean inativado = contaCaixaService.inativar(id);
        return inativado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Verificar e criar conta padrão",
            description = "Verifica se o usuário possui conta de caixa e cria uma conta padrão caso não possua.",
            tags = {"Fluxo de Caixa - Contas"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Verificação concluída",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ContaCaixaResponseDTO.class)
                    )
            )
    })
    @PostMapping("/garantir-conta-padrao")
    public ResponseEntity<List<ContaCaixaResponseDTO>> garantirContaPadrao(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        usuarioFluxoCaixaService.garantirContaCaixaPadrao(usuario);

        List<ContaCaixaResponseDTO> contas = contaCaixaService.listarContasAcessiveis(usuario.getId());
        return ResponseEntity.ok(contas);
    }
}
