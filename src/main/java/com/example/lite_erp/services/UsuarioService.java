package com.example.lite_erp.services;

import com.example.lite_erp.entities.categoria_usuario.CategoriasUsuario;
import com.example.lite_erp.entities.categoria_usuario.CategoriasUsuarioRepository;
import com.example.lite_erp.entities.usuario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriasUsuarioRepository categoriasUsuarioRepository;

    public List<Usuario> getUsuariosBloqueados() {
        return usuarioRepository.findByStatus("bloqueado");
    }

    public List<CategoriasResponseDTO> listarCategorias() {
        List<CategoriasUsuario> todas = categoriasUsuarioRepository.findAll();
        return todas.stream()
                .map(CategoriasResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> filtrarUsuarios(UsuarioFiltroDTO filtro) {
        Long categoriaId = filtro.categoriaId();
        List<Usuario> lista = usuarioRepository.filterUsuarios(categoriaId);
        return lista.stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(),
                        u.getNomeUsuario(),
                        u.getEmail(),
                        u.getStatus(),
                        u.getCategoria().getNome_categoria(),
                        u.getTelefone()
                ))
                .collect(Collectors.toList());
    }
}
