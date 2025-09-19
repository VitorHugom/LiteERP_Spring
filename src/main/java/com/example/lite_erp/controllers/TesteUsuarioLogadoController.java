package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.infra.security.AuthenticationUtils;
import com.example.lite_erp.services.UsuarioContaCaixaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para testar se o usuário logado está sendo obtido corretamente
 * REMOVER EM PRODUÇÃO - apenas para testes
 */
@RestController
@RequestMapping("/teste-usuario-logado")
public class TesteUsuarioLogadoController {

    @Autowired
    private UsuarioContaCaixaService usuarioContaCaixaService;

    @Operation(
            summary = "Testar obtenção do usuário logado",
            description = "Endpoint para testar se o usuário logado está sendo obtido corretamente do contexto de segurança.",
            tags = {"Teste - Usuário Logado"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Informações do usuário logado obtidas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    })
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> obterInfoUsuarioLogado(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Método 1: Via Authentication parameter
            Usuario usuarioViaAuth = (Usuario) authentication.getPrincipal();
            response.put("metodo1_authentication_parameter", Map.of(
                "id", usuarioViaAuth.getId(),
                "nome", usuarioViaAuth.getNomeUsuario(),
                "email", usuarioViaAuth.getEmail(),
                "role", usuarioViaAuth.getRole()
            ));
            
            // Método 2: Via AuthenticationUtils
            Usuario usuarioViaUtils = AuthenticationUtils.getUsuarioLogado();
            response.put("metodo2_authentication_utils", Map.of(
                "id", usuarioViaUtils.getId(),
                "nome", usuarioViaUtils.getNomeUsuario(),
                "email", usuarioViaUtils.getEmail(),
                "role", usuarioViaUtils.getRole()
            ));
            
            // Método 3: Conta de caixa padrão
            Long contaCaixaId = usuarioContaCaixaService.obterContaCaixaPadraoUsuarioLogado();
            response.put("conta_caixa_padrao_id", contaCaixaId);
            
            // Verificar se possui contas
            boolean possuiContas = usuarioContaCaixaService.usuarioLogadoPossuiContas();
            response.put("possui_contas_caixa", possuiContas);
            
            response.put("status", "sucesso");
            response.put("mensagem", "Usuário logado obtido com sucesso pelos dois métodos");
            
        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Erro ao obter usuário logado: " + e.getMessage());
            response.put("erro_detalhes", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Testar criação de conta caixa",
            description = "Endpoint para testar a criação automática de conta de caixa para o usuário logado.",
            tags = {"Teste - Usuário Logado"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Teste de criação de conta realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    })
    @PostMapping("/criar-conta-caixa")
    public ResponseEntity<Map<String, Object>> testarCriacaoContaCaixa() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Usuario usuarioLogado = AuthenticationUtils.getUsuarioLogado();
            
            // Verificar antes
            boolean possuiaContasAntes = usuarioContaCaixaService.usuarioLogadoPossuiContas();
            
            // Obter conta padrão (criará se não existir)
            Long contaCaixaId = usuarioContaCaixaService.obterContaCaixaPadraoUsuarioLogado();
            
            // Verificar depois
            boolean possuiContasDepois = usuarioContaCaixaService.usuarioLogadoPossuiContas();
            
            response.put("usuario_id", usuarioLogado.getId());
            response.put("usuario_nome", usuarioLogado.getNomeUsuario());
            response.put("possuia_contas_antes", possuiaContasAntes);
            response.put("possui_contas_depois", possuiContasDepois);
            response.put("conta_caixa_padrao_id", contaCaixaId);
            response.put("status", "sucesso");
            response.put("mensagem", "Teste de criação de conta realizado com sucesso");
            
        } catch (Exception e) {
            response.put("status", "erro");
            response.put("mensagem", "Erro no teste: " + e.getMessage());
            response.put("erro_detalhes", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }
}
