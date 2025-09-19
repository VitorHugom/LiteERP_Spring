package com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa;

import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovimentacaoCaixaRepository extends JpaRepository<MovimentacaoCaixa, Long> {

    List<MovimentacaoCaixa> findByContaCaixaIdOrderByDataMovimentacaoDescDataLancamentoDesc(Long contaCaixaId);

    Page<MovimentacaoCaixa> findByContaCaixaIdOrderByDataMovimentacaoDescDataLancamentoDesc(Long contaCaixaId, Pageable pageable);

    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "(:contaCaixaId IS NULL OR m.contaCaixa.id = :contaCaixaId) AND " +
            "(:tipoMovimentacaoId IS NULL OR m.tipoMovimentacao.id = :tipoMovimentacaoId) AND " +
            "(:categoria IS NULL OR m.tipoMovimentacao.categoria = :categoria) AND " +
            "(:centroCustoId IS NULL OR m.centroCusto.id = :centroCustoId) AND " +
            "(:tipoOrigem IS NULL OR m.tipoOrigem = :tipoOrigem) AND " +
            "(:dataInicio IS NULL OR m.dataMovimentacao >= :dataInicio) AND " +
            "(:dataFim IS NULL OR m.dataMovimentacao <= :dataFim) AND " +
            "(:valorMinimo IS NULL OR m.valor >= :valorMinimo) AND " +
            "(:valorMaximo IS NULL OR m.valor <= :valorMaximo) AND " +
            "(:status IS NULL OR m.status = :status) AND " +
            "(:usuarioResponsavelId IS NULL OR m.contaCaixa.usuarioResponsavel.id = :usuarioResponsavelId) AND " +
            "(:descricao = '' OR m.descricao LIKE CONCAT('%', :descricao, '%')) " +
            "ORDER BY m.dataMovimentacao DESC, m.dataLancamento DESC")
    Page<MovimentacaoCaixa> findByFiltros(
            @Param("contaCaixaId") Long contaCaixaId,
            @Param("tipoMovimentacaoId") Long tipoMovimentacaoId,
            @Param("categoria") TipoMovimentacao.CategoriaMovimentacao categoria,
            @Param("centroCustoId") Long centroCustoId,
            @Param("tipoOrigem") MovimentacaoCaixa.TipoOrigem tipoOrigem,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("valorMinimo") BigDecimal valorMinimo,
            @Param("valorMaximo") BigDecimal valorMaximo,
            @Param("status") MovimentacaoCaixa.StatusMovimentacao status,
            @Param("usuarioResponsavelId") Long usuarioResponsavelId,
            @Param("descricao") String descricao,
            Pageable pageable
    );

    @Query("SELECT SUM(m.valor) FROM MovimentacaoCaixa m WHERE " +
            "m.contaCaixa.id = :contaCaixaId AND " +
            "m.status = 'CONFIRMADO'")
    BigDecimal calcularSaldoContaCaixa(@Param("contaCaixaId") Long contaCaixaId);

    @Query("SELECT SUM(m.valor) FROM MovimentacaoCaixa m WHERE " +
            "m.contaCaixa.id = :contaCaixaId AND " +
            "m.status = 'CONFIRMADO' AND " +
            "m.dataMovimentacao BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularSaldoPorPeriodo(
            @Param("contaCaixaId") Long contaCaixaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "m.tipoOrigem = :tipoOrigem AND " +
            "m.referenciaId = :referenciaId")
    List<MovimentacaoCaixa> findByTipoOrigemAndReferenciaId(
            @Param("tipoOrigem") MovimentacaoCaixa.TipoOrigem tipoOrigem,
            @Param("referenciaId") Long referenciaId
    );

    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "m.contaCaixa.usuarioResponsavel.id = :usuarioId OR " +
            "EXISTS (SELECT 1 FROM Usuario u WHERE u.id = :usuarioId AND u.categoria.nome_categoria = 'ADMIN') " +
            "ORDER BY m.dataMovimentacao DESC, m.dataLancamento DESC")
    Page<MovimentacaoCaixa> findMovimentacoesAcessiveisPorUsuario(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "(m.contaCaixa.usuarioResponsavel.id = :usuarioId OR " +
            "EXISTS (SELECT 1 FROM Usuario u WHERE u.id = :usuarioId AND u.categoria.nome_categoria = 'ADMIN')) AND " +
            "(:dataInicio IS NULL OR m.dataMovimentacao >= :dataInicio) AND " +
            "(:dataFim IS NULL OR m.dataMovimentacao <= :dataFim) " +
            "ORDER BY m.dataMovimentacao DESC, m.dataLancamento DESC")
    Page<MovimentacaoCaixa> findMovimentacoesAcessiveisPorUsuarioComFiltroData(
            @Param("usuarioId") Long usuarioId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    // Métodos simplificados para filtrar por conta e datas
    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "m.contaCaixa.id = :contaCaixaId AND " +
            "m.dataMovimentacao >= :dataInicio " +
            "ORDER BY m.dataMovimentacao DESC, m.dataLancamento DESC")
    Page<MovimentacaoCaixa> findByContaCaixaIdAndDataMovimentacaoGreaterThanEqual(
            @Param("contaCaixaId") Long contaCaixaId,
            @Param("dataInicio") LocalDate dataInicio,
            Pageable pageable);

    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "m.contaCaixa.id = :contaCaixaId AND " +
            "m.dataMovimentacao <= :dataFim " +
            "ORDER BY m.dataMovimentacao DESC, m.dataLancamento DESC")
    Page<MovimentacaoCaixa> findByContaCaixaIdAndDataMovimentacaoLessThanEqual(
            @Param("contaCaixaId") Long contaCaixaId,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query("SELECT m FROM MovimentacaoCaixa m WHERE " +
            "m.contaCaixa.id = :contaCaixaId AND " +
            "m.dataMovimentacao >= :dataInicio AND " +
            "m.dataMovimentacao <= :dataFim " +
            "ORDER BY m.dataMovimentacao DESC, m.dataLancamento DESC")
    Page<MovimentacaoCaixa> findByContaCaixaIdAndDataMovimentacaoBetween(
            @Param("contaCaixaId") Long contaCaixaId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);
}
