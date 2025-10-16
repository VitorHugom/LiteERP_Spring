package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.recuperacao_senha.*;
import com.example.lite_erp.services.RecuperacaoSenhaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para recuperação de senha
 */
@RestController
@RequestMapping("/auth/recuperar-senha")
@Tag(name = "Autenticação", description = "Endpoints para recuperação de senha")
public class RecuperacaoSenhaController {
    
    @Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;
    
    /**
     * Endpoint 1: Solicitar recuperação de senha
     * Gera código de 6 dígitos e envia por email
     */
    @Operation(
        summary = "Solicitar recuperação de senha",
        description = "Gera um código de 6 dígitos e envia para o email do usuário cadastrado. " +
                     "O código expira em 15 minutos e permite até 3 tentativas de validação."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Código enviado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RecuperacaoSenhaResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Erro na solicitação (usuário não encontrado, sem email, etc.)"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro ao enviar email"
        )
    })
    @PostMapping("/solicitar")
    public ResponseEntity<RecuperacaoSenhaResponseDTO> solicitarRecuperacao(
            @Valid @RequestBody SolicitarRecuperacaoDTO dto,
            HttpServletRequest request
    ) {
        RecuperacaoSenhaResponseDTO response = recuperacaoSenhaService.solicitarRecuperacao(
            dto.nomeUsuario(),
            request
        );
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint 2: Validar código de recuperação
     * Valida o código e retorna token temporário
     */
    @Operation(
        summary = "Validar código de recuperação",
        description = "Valida o código de 6 dígitos recebido por email. " +
                     "Se válido, retorna um token temporário que deve ser usado para redefinir a senha. " +
                     "Permite até 3 tentativas. O token expira em 15 minutos."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Código validado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RecuperacaoSenhaResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Código inválido, expirado ou limite de tentativas excedido"
        )
    })
    @PostMapping("/validar-codigo")
    public ResponseEntity<RecuperacaoSenhaResponseDTO> validarCodigo(
            @Valid @RequestBody ValidarCodigoDTO dto
    ) {
        RecuperacaoSenhaResponseDTO response = recuperacaoSenhaService.validarCodigo(
            dto.nomeUsuario(),
            dto.codigo()
        );
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint 3: Redefinir senha
     * Usa o token temporário para redefinir a senha
     */
    @Operation(
        summary = "Redefinir senha",
        description = "Redefine a senha do usuário usando o token temporário recebido após validação do código. " +
                     "O token só pode ser usado uma vez e expira em 15 minutos."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Senha redefinida com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RecuperacaoSenhaResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Token inválido, expirado ou já utilizado"
        )
    })
    @PostMapping("/redefinir")
    public ResponseEntity<RecuperacaoSenhaResponseDTO> redefinirSenha(
            @Valid @RequestBody RedefinirSenhaDTO dto
    ) {
        RecuperacaoSenhaResponseDTO response = recuperacaoSenhaService.redefinirSenha(
            dto.token(),
            dto.novaSenha()
        );
        return ResponseEntity.ok(response);
    }
}

