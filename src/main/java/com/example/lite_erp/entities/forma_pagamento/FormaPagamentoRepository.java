package com.example.lite_erp.entities.forma_pagamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
    Page<FormaPagamento> findByDescricaoContainingIgnoreCaseOrderByDescricao(String descricao, Pageable pageable);
}
