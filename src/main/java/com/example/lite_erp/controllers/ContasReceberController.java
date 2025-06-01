package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.contas_receber.ContasReceberFiltroDTO;
import com.example.lite_erp.entities.contas_receber.ContasReceberRequestDTO;
import com.example.lite_erp.entities.contas_receber.ContasReceberResponseDTO;
import com.example.lite_erp.services.ContasReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/contas-receber")
public class ContasReceberController {

    @Autowired
    private ContasReceberService contasReceberService;

    @PostMapping
    public ContasReceberResponseDTO criarContaReceber(@RequestBody ContasReceberRequestDTO dto) {
        return contasReceberService.salvarContaReceber(dto);
    }

    @GetMapping
    public List<ContasReceberResponseDTO> listarContasReceber() {
        return contasReceberService.listarContasReceber();
    }

    @GetMapping("/{id}")
    public ContasReceberResponseDTO buscarContaReceberPorId(@PathVariable Integer id) {
        return contasReceberService.buscarContaReceberPorId(id);
    }

    @GetMapping("/buscar")
    public Page<ContasReceberResponseDTO> buscarContasReceberComFiltro(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable) {
        return contasReceberService.buscarContasReceberComFiltro(razaoSocial, dataInicio, dataFim, pageable);
    }

    @PutMapping("/{id}")
    public ContasReceberResponseDTO atualizarContaReceber(
            @PathVariable Integer id,
            @RequestBody ContasReceberRequestDTO dto) {
        return contasReceberService.atualizarContaReceber(id, dto);
    }

    @DeleteMapping("/{id}")
    public void excluirContaReceber(@PathVariable Integer id) {
        contasReceberService.excluirContaReceber(id);
    }

    @PostMapping("/relatorios")
    public ResponseEntity<List<ContasReceberResponseDTO>> gerarRelatorioContasReceber(
            @RequestBody ContasReceberFiltroDTO filtros
    ) {
        List<ContasReceberResponseDTO> dtos = contasReceberService.filtrarContasReceber(filtros);
        return ResponseEntity.ok(dtos);
    }
}
