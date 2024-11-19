package com.example.lite_erp.services;

import com.example.lite_erp.entities.itens_pedido.ItensPedido;
import com.example.lite_erp.entities.itens_pedido.ItensPedidoRepository;
import com.example.lite_erp.entities.itens_pedido.ItensPedidoRequestDTO;
import com.example.lite_erp.entities.pedidos.PedidosRepository;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItensPedidoService {

    @Autowired
    private ItensPedidoRepository itensPedidoRepository;

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;

    // Listar todos os itens de um pedido específico
    public List<ItensPedido> listarItensPorPedido(Long idPedido) {
        return itensPedidoRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(item -> item.getPedido().getId().equals(idPedido))
                .toList();
    }

    // Buscar um item de pedido por ID
    public Optional<ItensPedido> buscarPorId(Long id) {
        return itensPedidoRepository.findById(id);
    }

    // Criar novo item de pedido
    @Transactional
    public ItensPedido criarItemPedido(ItensPedidoRequestDTO dto) {
        ItensPedido itemPedido = new ItensPedido(
                null,
                pedidosRepository.findById(dto.idPedido()).orElseThrow(() -> new RuntimeException("Pedido não encontrado")),
                produtosRepository.findById(dto.idProduto()).orElseThrow(() -> new RuntimeException("Produto não encontrado")),
                dto.preco(),
                dto.quantidade()
        );

        ItensPedido savedItem = itensPedidoRepository.save(itemPedido);
        movimentoEstoqueService.registrarMovimentoPorPedido(savedItem);
        return savedItem;
    }

    // Atualizar item de pedido existente
    @Transactional
    public Optional<ItensPedido> atualizarItemPedido(Long id, ItensPedidoRequestDTO dto) {
        return itensPedidoRepository.findById(id).map(item -> {
            // Reverter o movimento de estoque do item atual
            movimentoEstoqueService.reverterMovimentoPorPedido(item);

            // Atualizar os dados do item
            item.setProduto(produtosRepository.findById(dto.idProduto()).orElseThrow(() -> new RuntimeException("Produto não encontrado")));
            item.setPreco(dto.preco());
            item.setQuantidade(dto.quantidade());

            ItensPedido updatedItem = itensPedidoRepository.save(item);

            // Registrar o novo movimento de estoque
            movimentoEstoqueService.registrarMovimentoPorPedido(updatedItem);

            return updatedItem;
        });
    }

    // Deletar item de pedido
    @Transactional
    public boolean deletarItemPedido(Long id) {
        Optional<ItensPedido> itemOptional = itensPedidoRepository.findById(id);
        if (itemOptional.isPresent()) {
            ItensPedido item = itemOptional.get();

            // Reverter o movimento de estoque antes de deletar o item
            movimentoEstoqueService.reverterMovimentoPorPedido(item);

            itensPedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}