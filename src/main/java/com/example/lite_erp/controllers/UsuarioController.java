package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.usuario.*;
import com.example.lite_erp.entities.vendedores.Vendedores;
import com.example.lite_erp.entities.vendedores.VendedoresRepository;
import com.example.lite_erp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")  // Configuração de CORS aplicada a todos os endpoints deste controlador
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private VendedoresRepository vendedoresRepository;

    @Autowired
    private UsuarioService usuarioService;


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
                        usuario.getTelefone(),
                        usuario.getStatus(),
                        usuario.getCategoria().getNome_categoria()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuariosDTO);
    }

    @PutMapping("/aprovar/{id}")
    public ResponseEntity<Map<String, String>> aprovarUsuario(@PathVariable String id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setStatus("autorizado"); // Mudando o status para autorizado
            usuarioRepository.save(usuario);

            // Verificar se a categoria do usuário é "Vendas"
            if (usuario.getCategoria() != null && usuario.getCategoria().getNome_categoria().equalsIgnoreCase("vendas")) {
                // Criar o vendedor e associar ao usuário
                Vendedores novoVendedor = new Vendedores();
                novoVendedor.setNome(usuario.getNomeUsuario());
                novoVendedor.setEmail(usuario.getEmail());
                novoVendedor.setTelefone(usuario.getTelefone());
                novoVendedor.setUsuario(usuario); // Vinculando o vendedor ao usuário

                // Salvar o vendedor no banco de dados
                vendedoresRepository.save(novoVendedor);
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário aprovado com sucesso.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
                    usuario.getTelefone(),
                    usuario.getStatus(),
                    usuario.getCategoria().getNome_categoria()
            );
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/relatorios")
    public ResponseEntity<List<UsuarioResponseDTO>> gerarRelatorioUsuarios(
            @RequestBody UsuarioFiltroDTO filtros
    ) {
        List<UsuarioResponseDTO> dtos = usuarioService.filtrarUsuarios(filtros);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriasResponseDTO>> getCategoriasUsuario() {
        List<CategoriasResponseDTO> categoriasDTO = usuarioService.listarCategorias();
        return ResponseEntity.ok(categoriasDTO);
    }
}
