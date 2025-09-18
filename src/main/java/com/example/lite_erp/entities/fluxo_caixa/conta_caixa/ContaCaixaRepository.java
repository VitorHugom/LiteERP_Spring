package com.example.lite_erp.entities.fluxo_caixa.conta_caixa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContaCaixaRepository extends JpaRepository<ContaCaixa, Long> {

    List<ContaCaixa> findByAtivoTrueOrderByDescricao();

    List<ContaCaixa> findByUsuarioResponsavelIdAndAtivoTrueOrderByDescricao(Long usuarioId);

    @Query("SELECT c FROM ContaCaixa c WHERE " +
            "(:usuarioId IS NULL OR c.usuarioResponsavel.id = :usuarioId) AND " +
            "(:tipo IS NULL OR c.tipo = :tipo) AND " +
            "(:ativo IS NULL OR c.ativo = :ativo) " +
            "ORDER BY c.descricao")
    List<ContaCaixa> findByFiltros(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") ContaCaixa.TipoConta tipo,
            @Param("ativo") Boolean ativo
    );

    @Query("SELECT c FROM ContaCaixa c WHERE " +
            "c.ativo = true AND " +
            "(c.usuarioResponsavel.id = :usuarioId OR " +
            " EXISTS (SELECT 1 FROM Usuario u WHERE u.id = :usuarioId AND u.categoria.nome_categoria = 'ADMIN')) " +
            "ORDER BY c.descricao")
    List<ContaCaixa> findContasAcessiveisPorUsuario(@Param("usuarioId") Long usuarioId);
}
