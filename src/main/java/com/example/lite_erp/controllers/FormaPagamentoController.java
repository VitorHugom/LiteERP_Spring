package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.forma_pagamento.*;
import com.example.lite_erp.services.FormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forma-pagamento")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoService formaPagamentoService;

    @PostMapping
    public ResponseEntity<FormaPagamentoResponseDTO> salvarFormaPagamento(@RequestBody FormaPagamentoRequestDTO dto) {
        FormaPagamentoResponseDTO response = formaPagamentoService.salvarFormaPagamento(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FormaPagamentoResponseDTO>> listarFormasPagamento() {
        List<FormaPagamentoResponseDTO> response = formaPagamentoService.listarFormasPagamento();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> buscarFormaPagamentoPorId(@PathVariable Long id) {
        FormaPagamentoResponseDTO response = formaPagamentoService.buscarFormaPagamentoPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/busca")
    public ResponseEntity<Page<FormaPagamentoResponseDTO>> buscarFormasPagamento(
            @RequestParam(required = false) String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "descricao") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<FormaPagamentoResponseDTO> response = formaPagamentoService.buscarFormasPagamento(descricao, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormaPagamentoResponseDTO> atualizarFormaPagamento(
            @PathVariable Long id,
            @RequestBody FormaPagamentoRequestDTO dto) {
        FormaPagamentoResponseDTO response = formaPagamentoService.atualizarFormaPagamento(id, dto);
        return ResponseEntity.ok(response);
    }

    // Endpoint para deletar forma de pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFormaPagamento(@PathVariable Long id) {
        formaPagamentoService.deletarFormaPagamento(id);
        return ResponseEntity.noContent().build();
    }
}
