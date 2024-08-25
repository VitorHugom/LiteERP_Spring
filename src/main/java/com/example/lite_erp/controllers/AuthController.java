package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.usuario.*;
import com.example.lite_erp.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository repository;
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
        Optional<Usuario> usuario = this.repository.findByNomeUsuario(body.nomeUsuario());

        if (usuario.isEmpty()) {
            Usuario novo_usuario = new Usuario();

            novo_usuario.setSenha(passwordEncoder.encode(body.senha()));
            novo_usuario.setEmail(body.email());
            novo_usuario.setNomeUsuario(body.nomeUsuario());
            novo_usuario.setCategoria_id(body.categoria_id());
            novo_usuario.setStatus("bloqueado"); // Usuário começa com status "bloqueado"
            this.repository.save(novo_usuario);

            return ResponseEntity.ok("User registered successfully. Awaiting authorization.");
        }

        return ResponseEntity.badRequest().build();
    }
}
