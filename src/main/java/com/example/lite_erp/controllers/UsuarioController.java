package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.entities.usuario.UsuarioRepository;
import com.example.lite_erp.entities.usuario.UsuarioResponseDTO;
import com.example.lite_erp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Configuração de CORS aplicada a todos os endpoints deste controlador
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getUser() {
        logger.info("Contexto de Segurança: {}", SecurityContextHolder.getContext().getAuthentication());

        // Recupera todos os usuários do banco de dados
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Converte a lista de entidades Usuario para uma lista de UsuarioDTO
        List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNomeUsuario(),
                        usuario.getEmail(),
                        usuario.getStatus(),
                        usuario.getCategoria().getNome_categoria()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuariosDTO);
    }

    @PutMapping("/aprovar/{id}")
    public ResponseEntity<String> aprovarUsuario(@PathVariable String id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setStatus("autorizado"); // Mudando o status para autorizado
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuário aprovado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    }

    @GetMapping("/bloqueados")
    public ResponseEntity<List<Usuario>> getUsuariosBloqueados() {
        List<Usuario> usuariosBloqueados = usuarioService.getUsuariosBloqueados();
        return ResponseEntity.ok(usuariosBloqueados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable String id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO(
                    usuario.getId(),
                    usuario.getNomeUsuario(),
                    usuario.getEmail(),
                    usuario.getStatus(),
                    usuario.getCategoria().getNome_categoria()
            );
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
