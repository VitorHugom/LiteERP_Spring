package com.example.lite_erp.controllers;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.clientes.ClientesRequestDTO;
import com.example.lite_erp.entities.clientes.ClientesResponseDTO;
import com.example.lite_erp.services.ClientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientesController {

    @Autowired
    private ClientesService clientesService;

    @GetMapping
    public List<ClientesResponseDTO> listarTodos() {
        List<Clientes> clientes = clientesService.listarTodos();
        return clientes.stream().map(ClientesResponseDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientesResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<Clientes> cliente = clientesService.buscarPorId(id);
        return cliente.map(value -> ResponseEntity.ok(new ClientesResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClientesResponseDTO> criarCliente(@RequestBody ClientesRequestDTO dto) {
        Clientes novoCliente = clientesService.criarCliente(dto);
        return ResponseEntity.ok(new ClientesResponseDTO(novoCliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientesResponseDTO> atualizarCliente(@PathVariable Long id, @RequestBody ClientesRequestDTO dto) {
        Optional<Clientes> clienteAtualizado = clientesService.atualizarCliente(id, dto);
        return clienteAtualizado.map(value -> ResponseEntity.ok(new ClientesResponseDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        if (clientesService.deletarCliente(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
