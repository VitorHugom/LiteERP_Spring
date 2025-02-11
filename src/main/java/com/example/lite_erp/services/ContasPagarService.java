package com.example.lite_erp.services;

import com.example.lite_erp.entities.contas_pagar.ContasPagar;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRepository;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRequestDTO;
import com.example.lite_erp.entities.contas_pagar.ContasPagarResponseDTO;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamentoRepository;
import com.example.lite_erp.entities.fornecedores.FornecedoresRepository;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContasPagarService {

    @Autowired
    private ContasPagarRepository contasPagarRepository;

    @Autowired
    private FornecedoresRepository fornecedorRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private TiposCobrancaRepository tiposCobrancaRepository;

    public ContasPagarResponseDTO salvarContaPagar(ContasPagarRequestDTO dto) {
        ContasPagar conta = new ContasPagar();

        conta.setFornecedor(fornecedorRepository.findById(dto.fornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado.")));

        conta.setFormaPagamento(formaPagamentoRepository.findById(dto.formaPagamentoId())
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada.")));

        conta.setTipoCobranca(tiposCobrancaRepository.findById(dto.tipoCobrancaId())
                .orElseThrow(() -> new RuntimeException("Tipo de cobrança não encontrado.")));

        conta.setNumeroDocumento(dto.numeroDocumento());
        conta.setParcela(dto.parcela());
        conta.setValorParcela(dto.valorParcela());
        conta.setValorTotal(dto.valorTotal());
        conta.setDataVencimento(dto.dataVencimento());
        conta.setStatus(dto.status());

        conta = contasPagarRepository.save(conta);

        return new ContasPagarResponseDTO(conta);
    }

    public List<ContasPagarResponseDTO> listarContasPagar() {
        return contasPagarRepository.findAll().stream()
                .map(ContasPagarResponseDTO::new)
                .toList();
    }

    public ContasPagarResponseDTO buscarContaPagarPorId(Long id) {
        ContasPagar conta = contasPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada."));
        return new ContasPagarResponseDTO(conta);
    }

    public Page<ContasPagarResponseDTO> buscarContasPagarComFiltro(
            String razaoSocial,
            LocalDate dataInicio,
            LocalDate dataFim,
            Pageable pageable) {

        Page<ContasPagar> contasPagar;

        System.out.println(razaoSocial);

        if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            // Busca somente por intervalo de datas
            contasPagar = contasPagarRepository.buscarPorIntervaloDeDatas(dataInicio, dataFim, pageable);
        } else {
            // Busca considerando razão social e intervalo de datas
            contasPagar = contasPagarRepository.buscarPorFiltro(razaoSocial, dataInicio, dataFim, pageable);
        }

        return contasPagar.map(ContasPagarResponseDTO::new);
    }

    public ContasPagarResponseDTO atualizarContaPagar(Long id, ContasPagarRequestDTO dto) {
        // Busca a conta existente
        ContasPagar contaExistente = contasPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada."));

        // Atualiza os campos
        contaExistente.setFornecedor(fornecedorRepository.findById(dto.fornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado.")));
        contaExistente.setFormaPagamento(formaPagamentoRepository.findById(dto.formaPagamentoId())
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada.")));
        contaExistente.setTipoCobranca(tiposCobrancaRepository.findById(dto.tipoCobrancaId())
                .orElseThrow(() -> new RuntimeException("Tipo de cobrança não encontrado.")));

        contaExistente.setNumeroDocumento(dto.numeroDocumento());
        contaExistente.setParcela(dto.parcela());
        contaExistente.setValorParcela(dto.valorParcela());
        contaExistente.setValorTotal(dto.valorTotal());
        contaExistente.setDataVencimento(dto.dataVencimento());
        contaExistente.setStatus(dto.status());

        // Salva as alterações
        ContasPagar contaAtualizada = contasPagarRepository.save(contaExistente);

        return new ContasPagarResponseDTO(contaAtualizada);
    }

    public void excluirContaPagar(Long id) {
        if (!contasPagarRepository.existsById(id)) {
            throw new RuntimeException("Conta a pagar não encontrada.");
        }

        contasPagarRepository.deleteById(id);
    }
}
