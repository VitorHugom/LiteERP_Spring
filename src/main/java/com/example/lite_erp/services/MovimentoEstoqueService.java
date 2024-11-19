package com.example.lite_erp.services;

import com.example.lite_erp.entities.estoque.EstoqueRequestDTO;
import com.example.lite_erp.entities.itens_pedido.ItensPedido;
import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadorias;
import com.example.lite_erp.entities.movimento_estoque.*;
import com.example.lite_erp.entities.produtos.Produtos;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MovimentoEstoqueService {

    @Autowired
    private MovimentoEstoqueRepository movimentoEstoqueRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Transactional
    public MovimentoEstoqueResponseDTO criarMovimentoAvulso(MovimentoEstoqueRequestDTO dto) {
        MovimentoEstoque movimento = new MovimentoEstoque();

        // Buscar o produto pelo ID
        Produtos produto = produtosRepository.findById(dto.idProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        movimento.setProduto(produto);
        movimento.setQtd(dto.qtd());
        movimento.setDataMovimentacao(dto.dataMovimentacao());

        // Salvar o movimento
        movimentoEstoqueRepository.save(movimento);

        // Atualizar o estoque com base no movimento
        atualizarEstoque(produto, dto.qtd());

        return new MovimentoEstoqueResponseDTO(movimento);
    }

    @Transactional
    public void registrarMovimentoPorPedido(ItensPedido itemPedido) {
        Produtos produto = buscarProduto(itemPedido.getProduto().getId());

        BigDecimal quantidadeMovimento = new BigDecimal(itemPedido.getQuantidade()).negate(); // Subtrai do estoque

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setItemPedido(itemPedido);
        movimento.setProduto(produto);
        movimento.setQtd(quantidadeMovimento);
        movimento.setDataMovimentacao(LocalDateTime.now());

        movimentoEstoqueRepository.save(movimento);

        // Atualizar o estoque
        atualizarEstoque(produto, quantidadeMovimento);
    }

    @Transactional
    public void registrarMovimentoPorRecebimento(ItensRecebimentoMercadorias itemRecebimento) {
        if (itemRecebimento.getId() == null) {
            throw new IllegalStateException("O item de recebimento precisa ser salvo antes de registrar um movimento de estoque.");
        }

        Produtos produto = buscarProduto(itemRecebimento.getProduto().getId());

        BigDecimal quantidadeMovimento = itemRecebimento.getQuantidade(); // Adiciona ao estoque

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setItemRecebimentoMercadoria(itemRecebimento);
        movimento.setProduto(produto);
        movimento.setQtd(quantidadeMovimento);
        movimento.setDataMovimentacao(LocalDateTime.now());

        movimentoEstoqueRepository.save(movimento);

        // Atualizar o estoque
        atualizarEstoque(produto, quantidadeMovimento);
    }

    @Transactional
    public void reverterMovimentoPorPedido(ItensPedido itemPedido) {
        Produtos produto = buscarProduto(itemPedido.getProduto().getId());

        BigDecimal quantidadeMovimento = new BigDecimal(itemPedido.getQuantidade()); // Adiciona de volta ao estoque

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setItemPedido(itemPedido);
        movimento.setProduto(produto);
        movimento.setQtd(quantidadeMovimento);
        movimento.setDataMovimentacao(LocalDateTime.now());

        movimentoEstoqueRepository.save(movimento);

        // Atualizar o estoque
        atualizarEstoque(produto, quantidadeMovimento);
    }

    @Transactional
    public void reverterMovimentoPorRecebimento(ItensRecebimentoMercadorias itemRecebimento) {
        Produtos produto = buscarProduto(itemRecebimento.getProduto().getId());

        BigDecimal quantidadeMovimento = itemRecebimento.getQuantidade().negate(); // Subtrai do estoque

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setItemRecebimentoMercadoria(itemRecebimento);
        movimento.setProduto(produto);
        movimento.setQtd(quantidadeMovimento);
        movimento.setDataMovimentacao(LocalDateTime.now());

        movimentoEstoqueRepository.save(movimento);

        // Atualizar o estoque
        atualizarEstoque(produto, quantidadeMovimento);
    }

    private Produtos buscarProduto(Long idProduto) {
        return produtosRepository.findById(idProduto)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    private void atualizarEstoque(Produtos produto, BigDecimal quantidade) {
        EstoqueRequestDTO estoqueRequest = new EstoqueRequestDTO(produto.getId(), quantidade);
        estoqueService.atualizarEstoque(estoqueRequest);
    }

    @Transactional(readOnly = true)
    public Page<MovimentoEstoqueResponseDTO> buscarMovimentoPorIntervaloDatas(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<MovimentoEstoque> movimentos = movimentoEstoqueRepository.findByDataMovimentacaoBetween(startDate, endDate, pageable);
        return movimentos.map(MovimentoEstoqueResponseDTO::new); // Convertendo para DTO
    }

    @Transactional(readOnly = true)
    public Page<MovimentoEstoqueResponseDTO> buscarTodosMovimentos(Pageable pageable) {
        Page<MovimentoEstoque> movimentos = movimentoEstoqueRepository.findAll(pageable);
        return movimentos.map(MovimentoEstoqueResponseDTO::new); // Convertendo para DTO
    }
}
