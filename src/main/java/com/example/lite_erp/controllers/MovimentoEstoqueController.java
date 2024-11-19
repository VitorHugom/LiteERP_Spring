package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.movimento_estoque.*;
import com.example.lite_erp.services.MovimentoEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/movimento-estoque")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovimentoEstoqueController {

    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;

    // Endpoint para criar um movimento de estoque avulso
    @PostMapping("/avulso")
    public ResponseEntity<MovimentoEstoqueResponseDTO> criarMovimentoAvulso(@RequestBody MovimentoEstoqueRequestDTO dto) {
        MovimentoEstoqueResponseDTO movimento = movimentoEstoqueService.criarMovimentoAvulso(dto);
        return ResponseEntity.ok(movimento);
    }

    @GetMapping("/busca-por-datas")
    public ResponseEntity<Page<MovimentoEstoqueResponseDTO>> buscarMovimentoPorIntervaloDatas(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Verifica se as datas foram fornecidas e se não estão vazias
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            // Ajuste o padrão do formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDate = LocalDate.parse(startDateStr, formatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(endDateStr, formatter).atTime(23, 59, 59);

            // Busca por intervalo de datas se as datas foram fornecidas
            Page<MovimentoEstoqueResponseDTO> movimentos = movimentoEstoqueService.buscarMovimentoPorIntervaloDatas(startDate, endDate, pageable);
            return ResponseEntity.ok(movimentos);
        } else {
            // Retorna todos os movimentos paginados se as datas não forem fornecidas
            Page<MovimentoEstoqueResponseDTO> movimentos = movimentoEstoqueService.buscarTodosMovimentos(pageable);
            return ResponseEntity.ok(movimentos);
        }
    }
}
