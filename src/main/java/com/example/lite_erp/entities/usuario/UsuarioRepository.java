package com.example.lite_erp.entities.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    List<Usuario> findByStatus(String status);
}
