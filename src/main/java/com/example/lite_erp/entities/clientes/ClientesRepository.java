package com.example.lite_erp.entities.clientes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ClientesRepository extends JpaRepository <Clientes, Long> {
    // Busca clientes pelo nome, ignorando letras maiúsculas e minúsculas, e com paginação
    Page<Clientes> findByRazaoSocialContainingIgnoreCase(String nome, Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.clientes.ClientesBuscaResponseDTO(" +
            "c.id, c.razaoSocial, v.nome) " +
            "FROM Clientes c " +
            "JOIN c.vendedor v " +
            "ORDER BY c.id DESC")
    Page<ClientesBuscaResponseDTO> findClientesForBusca(Pageable pageable);

    @Query("SELECT new com.example.lite_erp.entities.clientes.ClientesBuscaResponseDTO(" +
            "c.id, c.razaoSocial, v.nome) " +
            "FROM Clientes c " +
            "JOIN c.vendedor v " +
            "WHERE LOWER(c.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%')) " +
            "ORDER BY c.id DESC")
    Page<ClientesBuscaResponseDTO> findClientesForBuscaByRazaoSocial(@Param("razaoSocial") String razaoSocial, Pageable pageable);

    @Query("""
    SELECT new com.example.lite_erp.entities.clientes.ClientesBuscaResponseDTO(
        c.id, c.razaoSocial, v.nome
    )
    FROM Clientes c
    JOIN c.vendedor v
    WHERE c.dataNascimento >= COALESCE(:dataInicial, c.dataNascimento)
      AND c.dataNascimento <= COALESCE(:dataFinal,    c.dataNascimento)
      AND c.vendedor.id  = COALESCE(:vendedorId,      c.vendedor.id)
      AND c.cidade.id    = COALESCE(:cidadeId,        c.cidade.id)
    ORDER BY c.id DESC
""")
    List<ClientesBuscaResponseDTO> filterClientes(
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal")   LocalDate dataFinal,
            @Param("vendedorId")  Long vendedorId,
            @Param("cidadeId")    Long cidadeId
    );
}
