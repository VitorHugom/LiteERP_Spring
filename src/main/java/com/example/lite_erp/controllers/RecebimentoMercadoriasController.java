package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadorias;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasBuscaResponseDTO;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasRequestDTO;
import com.example.lite_erp.entities.recebimento_mercadorias.RecebimentoMercadoriasResponseDTO;
import com.example.lite_erp.services.RecebimentoMercadoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recebimento_mercadorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecebimentoMercadoriasController {
    @Autowired
    private RecebimentoMercadoriasService recebimentoMercadoriasService;

    @GetMapping
    public List<RecebimentoMercadoriasResponseDTO> listarTodos(){
        return recebimentoMercadoriasService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecebimentoMercadoriasResponseDTO> buscarPorId(@PathVariable Integer id) {
        Optional<RecebimentoMercadorias> recebimento = recebimentoMercadoriasService.buscarPorId(id);
        return recebimento.map(value -> ResponseEntity.ok(new RecebimentoMercadoriasResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecebimentoMercadoriasResponseDTO> criarRecebimento(@RequestBody RecebimentoMercadoriasRequestDTO dto) {
        RecebimentoMercadorias novoRecebimento = recebimentoMercadoriasService.criarRecebimento(dto);
        return ResponseEntity.ok(new RecebimentoMercadoriasResponseDTO(novoRecebimento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecebimentoMercadoriasResponseDTO> atualizarRecebimento(@PathVariable Integer id, @RequestBody RecebimentoMercadoriasRequestDTO dto) {
        Optional<RecebimentoMercadorias> recebimentoAtualizado = recebimentoMercadoriasService.atualizarRecebimento(id, dto);
        return recebimentoAtualizado.map(value -> ResponseEntity.ok(new RecebimentoMercadoriasResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRecebimento(@PathVariable Integer id) {
        if (recebimentoMercadoriasService.deletarRecebimento(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/busca")
    public Page<RecebimentoMercadoriasBuscaResponseDTO> buscarRecebimentos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dataRecebimento").descending());
        return recebimentoMercadoriasService.buscarRecebimentos(pageRequest);
    }

    @GetMapping("/busca/{id}")
    public ResponseEntity<RecebimentoMercadoriasBuscaResponseDTO> simplesBuscaPorId(@PathVariable Integer id) {
        return recebimentoMercadoriasService.simplesBuscaPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/busca-por-razao-social")
    public ResponseEntity<Page<RecebimentoMercadoriasBuscaResponseDTO>> buscarRecebimentosPorRazaoSocial(
            @RequestParam String razaoSocial,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RecebimentoMercadoriasBuscaResponseDTO> recebimentos = recebimentoMercadoriasService.buscarRecebimentosPorRazaoSocial(razaoSocial, pageable);
        return ResponseEntity.ok(recebimentos);
    }
}
