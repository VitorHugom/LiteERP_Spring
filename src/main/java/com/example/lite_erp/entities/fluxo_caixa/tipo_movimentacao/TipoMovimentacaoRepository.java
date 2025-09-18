package com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TipoMovimentacaoRepository extends JpaRepository<TipoMovimentacao, Long> {

    List<TipoMovimentacao> findByAtivoTrueOrderByDescricao();

    List<TipoMovimentacao> findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao categoria);

    @Query("SELECT t FROM TipoMovimentacao t WHERE " +
            "(:categoria IS NULL OR t.categoria = :categoria) AND " +
            "(:ativo IS NULL OR t.ativo = :ativo) " +
            "ORDER BY t.descricao")
    List<TipoMovimentacao> findByFiltros(
            @Param("categoria") TipoMovimentacao.CategoriaMovimentacao categoria,
            @Param("ativo") Boolean ativo
    );

    Optional<TipoMovimentacao> findByDescricaoContainingIgnoreCase(String descricao);
}
