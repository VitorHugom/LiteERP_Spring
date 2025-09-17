package com.example.lite_erp.services;

import com.example.lite_erp.entities.contas_pagar.*;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamentoRepository;
import com.example.lite_erp.entities.fornecedores.FornecedoresRepository;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            Long idFornecedor,
            Boolean somentePagar,
            Pageable pageable) {

        Page<ContasPagar> contasPagar;

        if (idFornecedor != null) {
            String razaoSocialParam = (razaoSocial != null && !razaoSocial.trim().isEmpty())
                    ? "%" + razaoSocial + "%"
                    : null;
            if (Boolean.TRUE.equals(somentePagar)) {
                contasPagar = contasPagarRepository.buscarPorFornecedorComFiltroSomentePagar(idFornecedor, razaoSocialParam, dataInicio, dataFim, pageable);
            } else {
                contasPagar = contasPagarRepository.buscarPorFornecedorComFiltro(idFornecedor, razaoSocialParam, dataInicio, dataFim, pageable);
            }
        } else if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            if (Boolean.TRUE.equals(somentePagar)) {
                contasPagar = contasPagarRepository.buscarPorIntervaloDeDatasSomentePagar(dataInicio, dataFim, pageable);
            } else {
                contasPagar = contasPagarRepository.buscarPorIntervaloDeDatas(dataInicio, dataFim, pageable);
            }
        } else {
            if (Boolean.TRUE.equals(somentePagar)) {
                contasPagar = contasPagarRepository.buscarPorFiltroSomentePagar(razaoSocial, dataInicio, dataFim, pageable);
            } else {
                contasPagar = contasPagarRepository.buscarPorFiltro(razaoSocial, dataInicio, dataFim, pageable);
            }
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

    public List<ContasPagarResponseDTO> filtrarContasPagar(ContasPagarFiltroDTO filtro) {
        List<ContasPagar> lista = contasPagarRepository.filterContasPagar(
                filtro.dataVencimentoInicio(),
                filtro.dataVencimentoFim(),
                filtro.idFornecedor(),
                filtro.idTipoCobranca(),
                filtro.idFormaPagamento(),
                filtro.valorTotalInicial(),
                filtro.valorTotalFinal()
        );
        return lista.stream()
                .map(ContasPagarResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Page<ContasPagarRelatorioResponseDTO> gerarRelatorioContasPorData(
            ContasPagarRelatorioFiltroDTO filtro,
            Pageable pageable) {

        String statusFiltro = filtro.status();

        Page<ContasPagarRelatorioProjection> projections = contasPagarRepository.buscarRelatorioContasPorData(
                statusFiltro,
                filtro.dataInicio(),
                filtro.dataFim(),
                filtro.idFornecedor(),
                filtro.idFormaPagamento(),
                pageable
        );

        return projections.map(projection -> {
            List<Long> idsParcelas = Arrays.stream(projection.getIdsParcelas().split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            return new ContasPagarRelatorioResponseDTO(
                    projection.getDataVencimento(),
                    projection.getValorTotalParcelas(),
                    projection.getQtdParcelas(),
                    idsParcelas
            );
        });
    }

    public ContasPagarResponseDTO realizarPagamento(Long id) {
        ContasPagar conta = contasPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada."));

        if ("paga".equals(conta.getStatus())) {
            throw new RuntimeException("Esta conta já foi paga.");
        }

        conta.setStatus("paga");

        ContasPagar contaAtualizada = contasPagarRepository.save(conta);
        return new ContasPagarResponseDTO(contaAtualizada);
    }
}
