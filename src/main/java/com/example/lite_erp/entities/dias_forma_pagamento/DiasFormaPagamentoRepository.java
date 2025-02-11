package com.example.lite_erp.entities.dias_forma_pagamento;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DiasFormaPagamentoRepository extends JpaRepository<DiasFormaPagamento, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM DiasFormaPagamento d WHERE d.formaPagamento.id = :formaPagamentoId")
    void deleteByFormaPagamentoId(Long formaPagamentoId);
}
