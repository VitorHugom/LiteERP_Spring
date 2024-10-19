package com.example.lite_erp.entities.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Long> {
    List<Pedidos> findByStatusOrderByIdDesc(String status);
}
