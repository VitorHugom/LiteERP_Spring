package com.example.lite_erp.entities.fluxo_caixa.centro_custo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CentroCustoRepository extends JpaRepository<CentroCusto, Long> {

    List<CentroCusto> findByAtivoTrueOrderByDescricao();

    Optional<CentroCusto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}
