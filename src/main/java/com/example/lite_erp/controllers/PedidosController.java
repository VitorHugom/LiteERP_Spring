package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.pedidos.Pedidos;
import com.example.lite_erp.entities.pedidos.PedidosRequestDTO;
import com.example.lite_erp.entities.pedidos.PedidosResponseDTO;
import com.example.lite_erp.services.PedidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PedidosController {

    @Autowired
    private PedidosService pedidosService;

    @GetMapping
    public List<PedidosResponseDTO> listarTodos() {
        List<Pedidos> pedidos = pedidosService.listarTodos();
        return pedidos.stream().map(PedidosResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidosResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<Pedidos> pedido = pedidosService.buscarPorId(id);
        return pedido.map(value -> ResponseEntity.ok(new PedidosResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PedidosResponseDTO> criarPedido(@RequestBody PedidosRequestDTO dto) {
        Pedidos novoPedido = pedidosService.criarPedido(dto);
        return ResponseEntity.ok(new PedidosResponseDTO(novoPedido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidosResponseDTO> atualizarPedido(@PathVariable Long id, @RequestBody PedidosRequestDTO dto) {
        Optional<Pedidos> pedidoAtualizado = pedidosService.atualizarPedido(id, dto);
        return pedidoAtualizado.map(value -> ResponseEntity.ok(new PedidosResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        if (pedidosService.deletarPedido(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
