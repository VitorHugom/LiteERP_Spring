package com.example.lite_erp.services;

import com.example.lite_erp.entities.dias_forma_pagamento.DiasFormaPagamento;
import com.example.lite_erp.entities.dias_forma_pagamento.DiasFormaPagamentoRepository;
import com.example.lite_erp.entities.forma_pagamento.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormaPagamentoService {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private DiasFormaPagamentoRepository diasFormaPagamentoRepository;

    public FormaPagamentoResponseDTO salvarFormaPagamento(FormaPagamentoRequestDTO dto) {
        // Criar a entidade FormaPagamento
        FormaPagamento formaPagamento = new FormaPagamento();
        formaPagamento.setDescricao(dto.descricao());

        // Criar os DiasFormaPagamento associados e vincular diretamente
        List<DiasFormaPagamento> diasFormaPagamento = dto.diasFormaPagamento()
                .stream()
                .map(dias -> new DiasFormaPagamento(null, formaPagamento, dias))
                .toList();

        formaPagamento.setDiasFormaPagamento(diasFormaPagamento);

        // Salvar FormaPagamento junto com os DiasFormaPagamento
        FormaPagamento formaPagamentoSalva = formaPagamentoRepository.save(formaPagamento);

        return new FormaPagamentoResponseDTO(formaPagamentoSalva);
    }

    public List<FormaPagamentoResponseDTO> listarFormasPagamento() {
        return formaPagamentoRepository.findAll()
                .stream()
                .map(FormaPagamentoResponseDTO::new)
                .toList();
    }

    public FormaPagamentoResponseDTO buscarFormaPagamentoPorId(Long id) {
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada."));
        return new FormaPagamentoResponseDTO(formaPagamento);
    }

    public Page<FormaPagamentoResponseDTO> buscarFormasPagamento(String descricao, Pageable pageable) {
        Page<FormaPagamento> page;
        if (descricao != null && !descricao.isEmpty()) {
            page = formaPagamentoRepository.findByDescricaoContainingIgnoreCaseOrderByDescricao(descricao, pageable);
        } else {
            page = formaPagamentoRepository.findAll(pageable);
        }
        return page.map(FormaPagamentoResponseDTO::new);
    }

    public FormaPagamentoResponseDTO atualizarFormaPagamento(Long id, FormaPagamentoRequestDTO dto) {
        // Buscar forma de pagamento existente
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada."));

        // Atualizar os dados básicos
        formaPagamento.setDescricao(dto.descricao());

        // Atualizar DiasFormaPagamento
        // Remove os dias antigos e adiciona os novos diretamente na lista gerenciada pelo Hibernate
        formaPagamento.getDiasFormaPagamento().clear(); // Mantém a referência da lista gerenciada
        List<DiasFormaPagamento> novosDias = dto.diasFormaPagamento()
                .stream()
                .map(dias -> new DiasFormaPagamento(null, formaPagamento, dias))
                .collect(Collectors.toList());
        formaPagamento.getDiasFormaPagamento().addAll(novosDias);

        // Salvar forma de pagamento atualizada
        FormaPagamento formaPagamentoAtualizada = formaPagamentoRepository.save(formaPagamento);

        return new FormaPagamentoResponseDTO(formaPagamentoAtualizada);
    }

    public void deletarFormaPagamento(Long id) {
        // Verificar se a forma de pagamento existe
        FormaPagamento formaPagamento = formaPagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada."));

        // Deletar a entidade
        formaPagamentoRepository.delete(formaPagamento);
    }
}

