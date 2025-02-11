package com.example.lite_erp.services;

import com.example.lite_erp.entities.contas_pagar.ContasPagar;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRepository;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamento;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamentoRepository;
import com.example.lite_erp.entities.fornecedores.FornecedoresRepository;
import com.example.lite_erp.entities.itens_recebimento_mercadorias.ItensRecebimentoMercadorias;
import com.example.lite_erp.entities.produtos.ProdutosRepository;
import com.example.lite_erp.entities.recebimento_mercadorias.*;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecebimentoMercadoriasService {

    @Autowired
    private RecebimentoMercadoriasRepository recebimentoMercadoriasRepository;

    @Autowired
    private FornecedoresRepository fornecedoresRepository;

    @Autowired
    private TiposCobrancaRepository tiposCobrancaRepository;

    @Autowired
    private ProdutosRepository produtosRepository;

    @Autowired
    private MovimentoEstoqueService movimentoEstoqueService;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private ContasPagarRepository contasPagarRepository;

    public List<RecebimentoMercadoriasResponseDTO> listarTodos() {
        return recebimentoMercadoriasRepository.findAll()
                .stream()
                .map(RecebimentoMercadoriasResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<RecebimentoMercadorias> buscarPorId(Integer id) {
        return recebimentoMercadoriasRepository.findById(id);
    }

    public Optional<RecebimentoMercadoriasBuscaResponseDTO> simplesBuscaPorId(Integer id) {
        return recebimentoMercadoriasRepository.findById(id)
                .map(RecebimentoMercadoriasBuscaResponseDTO::new);
    }

    @Transactional
    public RecebimentoMercadorias criarRecebimento(RecebimentoMercadoriasRequestDTO dto) {
        RecebimentoMercadorias recebimento = new RecebimentoMercadorias();
        recebimento.setFornecedor(fornecedoresRepository.findById(dto.idFornecedor()).orElseThrow());
        recebimento.setTipoCobranca(tiposCobrancaRepository.findById(dto.idTipoCobranca()).orElseThrow());
        recebimento.setDataRecebimento(dto.dataRecebimento());
        recebimento.setFormaPagamento(formaPagamentoRepository.findById(dto.idFormaPagamento()).orElseThrow());

        // Criar e associar os itens ao recebimento
        dto.itens().forEach(itemDto -> {
            ItensRecebimentoMercadorias item = new ItensRecebimentoMercadorias();
            item.setProduto(produtosRepository.findById(itemDto.idProduto()).orElseThrow());
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());
            item.setRecebimento(recebimento);
            recebimento.getItens().add(item);
        });

        // Salvar recebimento e seus itens
        RecebimentoMercadorias recebimentoSalvo = recebimentoMercadoriasRepository.save(recebimento);

        // Após salvar, registrar movimentos de estoque
        recebimentoSalvo.getItens().forEach(movimentoEstoqueService::registrarMovimentoPorRecebimento);

        // Gera as contas a pagar para o recebimento criado
        gerarContasAPagarPorRecebimento(recebimentoSalvo);

        return recebimentoSalvo;
    }

    @Transactional
    public Optional<RecebimentoMercadorias> atualizarRecebimento(Integer id, RecebimentoMercadoriasRequestDTO dto) {
        return recebimentoMercadoriasRepository.findById(id).map(recebimento -> {
            // Remover contas a pagar existentes antes de atualizar o recebimento
            removerContasAPagarPorRecebimento(recebimento);

            // Atualizar dados do recebimento
            recebimento.setFornecedor(fornecedoresRepository.findById(dto.idFornecedor()).orElseThrow());
            recebimento.setTipoCobranca(tiposCobrancaRepository.findById(dto.idTipoCobranca()).orElseThrow());
            recebimento.setDataRecebimento(dto.dataRecebimento());
            recebimento.setFormaPagamento(formaPagamentoRepository.findById(dto.idFormaPagamento()).orElseThrow());

            // Reverter movimentos antigos associados aos itens removidos
            recebimento.getItens().forEach(item ->
                    movimentoEstoqueService.reverterMovimentoPorRecebimento(item)
            );
            recebimento.getItens().clear();

            // Recria a lista de itens com os dados do DTO
            dto.itens().forEach(itemDto -> {
                ItensRecebimentoMercadorias item = new ItensRecebimentoMercadorias();
                item.setProduto(produtosRepository.findById(itemDto.idProduto()).orElseThrow());
                item.setQuantidade(itemDto.quantidade());
                item.setValorUnitario(itemDto.valorUnitario());
                item.setRecebimento(recebimento);
                recebimento.getItens().add(item);
            });

            // Salvar recebimento e seus itens
            RecebimentoMercadorias recebimentoSalvo = recebimentoMercadoriasRepository.save(recebimento);

            // Registrar os novos movimentos de estoque
            recebimentoSalvo.getItens().forEach(movimentoEstoqueService::registrarMovimentoPorRecebimento);

            // Gera as contas a pagar novamente para o recebimento atualizado
            gerarContasAPagarPorRecebimento(recebimentoSalvo);

            return recebimentoSalvo;
        });
    }

    @Transactional
    public boolean deletarRecebimento(Integer id) {
        return recebimentoMercadoriasRepository.findById(id).map(recebimento -> {
            // Reverter movimentos de estoque para os itens associados
            recebimento.getItens().forEach(item ->
                    movimentoEstoqueService.reverterMovimentoPorRecebimento(item)
            );

            // Remover contas a pagar associadas ao recebimento
            removerContasAPagarPorRecebimento(recebimento);

            // Excluir recebimento
            recebimentoMercadoriasRepository.delete(recebimento);
            return true;
        }).orElse(false);
    }

    public Page<RecebimentoMercadoriasBuscaResponseDTO> buscarRecebimentos(Pageable pageable) {
        return recebimentoMercadoriasRepository.findAllOrderedByDataRecebimento(pageable);
    }

    public Page<RecebimentoMercadoriasBuscaResponseDTO> buscarRecebimentosPorRazaoSocial(String razaoSocial, Pageable pageable) {
        return recebimentoMercadoriasRepository.findRecebimentosByFornecedorRazaoSocial(razaoSocial + "%", pageable);
    }

    @Transactional
    public void gerarContasAPagarPorRecebimento(RecebimentoMercadorias recebimento) {
        FormaPagamento formaPagamento = recebimento.getFormaPagamento();

        if (formaPagamento == null || formaPagamento.getDiasFormaPagamento().isEmpty()) {
            throw new RuntimeException("Forma de pagamento não definida ou sem parcelamento configurado.");
        }

        // Calcula o valor total do recebimento
        BigDecimal valorTotal = recebimento.getItens().stream()
                .map(item -> item.getQuantidade().multiply(item.getValorUnitario()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalParcelas = formaPagamento.getDiasFormaPagamento().size();
        BigDecimal valorParcela = valorTotal.divide(BigDecimal.valueOf(totalParcelas), 2, RoundingMode.HALF_UP);

        final int[] contadorParcela = {1};

        formaPagamento.getDiasFormaPagamento().forEach(dia -> {
            ContasPagar conta = new ContasPagar();
            conta.setFornecedor(recebimento.getFornecedor());
            conta.setFormaPagamento(formaPagamento);
            conta.setTipoCobranca(recebimento.getTipoCobranca());
            conta.setNumeroDocumento("REC-" + recebimento.getIdRecebimento());
            conta.setParcela(contadorParcela[0]);
            conta.setValorParcela(valorParcela);
            conta.setDataVencimento(recebimento.getDataRecebimento().plusDays(dia.getDiasParaVencimento()));
            conta.setStatus("aberta");

            // Adiciona o valor total ao registro da conta
            conta.setValorTotal(valorTotal);

            contadorParcela[0]++;

            // Salvar conta no repositório
            contasPagarRepository.save(conta);
        });
    }

    @Transactional
    private void removerContasAPagarPorRecebimento(RecebimentoMercadorias recebimento) {
        List<ContasPagar> contas = contasPagarRepository.findByNumeroDocumento("REC-" + recebimento.getIdRecebimento());
        contasPagarRepository.deleteAll(contas);
    }
}