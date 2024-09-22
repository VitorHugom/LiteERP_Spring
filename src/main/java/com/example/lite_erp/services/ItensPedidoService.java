package com.example.lite_erp.services;

import com.example.lite_erp.entities.itens_pedido.ItensPedido;
import com.example.lite_erp.entities.itens_pedido.ItensPedidoRepository;
import com.example.lite_erp.entities.itens_pedido.ItensPedidoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItensPedidoService {

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

    // Listar todos os itens de um pedido específico
    public List<ItensPedido> listarItensPorPedido(Long idPedido) {
        return itensPedidoRepository.findAll().stream()
                .filter(item -> item.getIdPedido().equals(idPedido))
                .toList();
    }

    // Buscar um item de pedido por ID
    public Optional<ItensPedido> buscarPorId(Long id) {
        return itensPedidoRepository.findById(id);
    }

    // Criar novo item de pedido
    public ItensPedido criarItemPedido(ItensPedidoRequestDTO dto) {
        ItensPedido itemPedido = new ItensPedido(
                null,
                dto.idPedido(),
                dto.idProduto(),
                dto.preco(),
                dto.quantidade()
        );
        return itensPedidoRepository.save(itemPedido);
    }

    // Atualizar item de pedido existente
    public Optional<ItensPedido> atualizarItemPedido(Long id, ItensPedidoRequestDTO dto) {
        return itensPedidoRepository.findById(id).map(item -> {
            item.setIdProduto(dto.idProduto());
            item.setPreco(dto.preco());
            item.setQuantidade(dto.quantidade());
            return itensPedidoRepository.save(item);
        });
    }

    // Deletar item de pedido
    public boolean deletarItemPedido(Long id) {
        if (itensPedidoRepository.existsById(id)) {
            itensPedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
