package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.categoria_usuario.CategoriasUsuarioRepository;
import com.example.lite_erp.entities.usuario.*;
import com.example.lite_erp.entities.vendedores.Vendedores;
import com.example.lite_erp.entities.vendedores.VendedoresRepository;
import com.example.lite_erp.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        Usuario usuario = this.repository.findByNomeUsuario(body.nomeUsuario()).orElseThrow(() -> new RuntimeException("User not found"));

        // Verifica se o usuário está autorizado antes de gerar o token
        if (!"autorizado".equals(usuario.getStatus())) {
            return ResponseEntity.status(403).body("User not authorized");
        }

        if (passwordEncoder.matches(body.senha(), usuario.getSenha())) {
            String token = this.tokenService.generateToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(usuario.getNomeUsuario(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<Usuario> usuarioExistente = this.repository.findByNomeUsuario(body.nomeUsuario());

        if (usuarioExistente.isEmpty()) {
            System.out.println("Criando usuario");
            Usuario novoUsuario = new Usuario();
            novoUsuario.setSenha(passwordEncoder.encode(body.senha()));
            novoUsuario.setEmail(body.email());
            novoUsuario.setNomeUsuario(body.nomeUsuario());
            novoUsuario.setCategoria_id(body.categoria_id());
            novoUsuario.setTelefone(body.telefone());
            novoUsuario.setStatus("bloqueado");
            this.repository.save(novoUsuario);

            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        }

        return ResponseEntity.badRequest().build();
    }
}
