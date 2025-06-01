package com.example.lite_erp.entities.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, String> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

    List<Usuario> findByStatus(String status);

    @Query("""
        SELECT u
        FROM Usuario u
        JOIN u.categoria c
        WHERE c.id = COALESCE(:categoriaId, c.id)
        """)
    List<Usuario> filterUsuarios(
            @Param("categoriaId") Long categoriaId
    );
}
