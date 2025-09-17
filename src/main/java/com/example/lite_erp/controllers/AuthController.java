package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.categoria_usuario.CategoriasUsuarioRepository;
import com.example.lite_erp.entities.usuario.*;
import com.example.lite_erp.entities.vendedores.VendedoresRepository;
import com.example.lite_erp.infra.exceptions.BusinessException;
import com.example.lite_erp.infra.exceptions.ResourceNotFoundException;
import com.example.lite_erp.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    private final UsuarioRepository repository;
    private final VendedoresRepository vendedoresRepository;
    private final CategoriasUsuarioRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    @Operation(
            summary = "Autentica um usuário e retorna um token JWT",
            description = "Requer um nome de usuário e senha válidos. Retorna um token JWT se a autenticação for bem-sucedida.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login bem-sucedido",
                            content = @Content(schema = @Schema(implementation = LoginResponseDTO.class),
                                    examples = @ExampleObject(
                                            name = "Exemplo de resposta",
                                            value = "{ \"nome\": \"example\", \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\" }"
                                    ))),
                    @ApiResponse(responseCode = "401", description = "Senha incorreta",
                            content = @Content(schema = @Schema(example = "Senha incorreta"))),
                    @ApiResponse(responseCode = "403", description = "Usuário não autorizado",
                            content = @Content(schema = @Schema(example = "Usuário não autorizado"))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                            content = @Content(schema = @Schema(example = "Usuário não encontrado")))
            }
    )
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        // Busca o usuário pelo nome de usuário
        Usuario usuario = this.repository.findByNomeUsuario(body.nomeUsuario())
                .orElseThrow(() -> ResourceNotFoundException.usuario(body.nomeUsuario()));

        // Verifica o status do usuário
        if (!"autorizado".equals(usuario.getStatus())) {
            throw BusinessException.usuarioNaoAutorizado(usuario.getNomeUsuario());
        }

        // Verifica a senha
        if (!passwordEncoder.matches(body.senha(), usuario.getSenha())) {
            throw BusinessException.senhaIncorreta();
        }

        // Gera o token se tudo estiver correto
        String token = this.tokenService.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponseDTO(usuario.getNomeUsuario(), token));
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        // Verificar se o usuário já existe
        if (this.repository.findByNomeUsuario(body.nomeUsuario()).isPresent()) {
            throw BusinessException.usuarioJaExiste(body.nomeUsuario());
        }

        // Criar novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setSenha(passwordEncoder.encode(body.senha()));
        novoUsuario.setEmail(body.email());
        novoUsuario.setNomeUsuario(body.nomeUsuario());
        novoUsuario.setCategoria_id(body.categoria_id());
        novoUsuario.setTelefone(body.telefone());
        novoUsuario.setStatus("bloqueado");
        this.repository.save(novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Usuário registrado com sucesso"));
    }
}
