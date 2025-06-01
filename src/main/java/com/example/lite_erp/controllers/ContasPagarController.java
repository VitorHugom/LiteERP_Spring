package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.contas_pagar.ContasPagarFiltroDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRequestDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarResponseDTO;
import com.example.lite_erp.services.ContasPagarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas-pagar")
public class ContasPagarController {

    @Autowired
    private ContasPagarService contasPagarService;

    @PostMapping
    public ContasPagarResponseDTO criarContaPagar(@RequestBody ContasPagarRequestDTO dto) {
        return contasPagarService.salvarContaPagar(dto);
    }

    @GetMapping
    public List<ContasPagarResponseDTO> listarContasPagar() {
        return contasPagarService.listarContasPagar();
    }

    @GetMapping("/{id}")
    public ContasPagarResponseDTO buscarContaPagarPorId(@PathVariable Long id) {
        return contasPagarService.buscarContaPagarPorId(id);
    }

    @GetMapping("/buscar")
    public Page<ContasPagarResponseDTO> buscarContasPagarComFiltro(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable) {

        return contasPagarService.buscarContasPagarComFiltro(razaoSocial, dataInicio, dataFim, pageable);
    }

    @PutMapping("/{id}")
    public ContasPagarResponseDTO atualizarContaPagar(@PathVariable Long id, @RequestBody ContasPagarRequestDTO dto) {
        return contasPagarService.atualizarContaPagar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirContaPagar(@PathVariable Long id) {
        contasPagarService.excluirContaPagar(id);
    }


    @PostMapping("/relatorios")
    public ResponseEntity<List<ContasPagarResponseDTO>> gerarRelatorioContasPagar(
            @RequestBody ContasPagarFiltroDTO filtros
    ) {
        List<ContasPagarResponseDTO> dtos = contasPagarService.filtrarContasPagar(filtros);
        return ResponseEntity.ok(dtos);
    }
}
