package com.example.lite_erp.services;

import com.example.lite_erp.entities.contas_receber.*;
import com.example.lite_erp.entities.clientes.ClientesRepository;
import com.example.lite_erp.entities.forma_pagamento.FormaPagamentoRepository;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContasReceberService {

    @Autowired
    private ContasReceberRepository contasReceberRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Autowired
    private TiposCobrancaRepository tiposCobrancaRepository;

    public ContasReceberResponseDTO salvarContaReceber(ContasReceberRequestDTO dto) {
        ContasReceber conta = new ContasReceber();

        conta.setCliente(clientesRepository.findById(dto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado.")));
        conta.setFormaPagamento(formaPagamentoRepository.findById(dto.idFormaPagamento())
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada.")));
        conta.setTiposCobranca(tiposCobrancaRepository.findById(dto.idTipoCobranca())
                .orElseThrow(() -> new RuntimeException("Tipo de cobrança não encontrado.")));

        conta.setNumeroDocumento(dto.numeroDocumento());
        conta.setParcela(dto.parcela());
        conta.setValorParcela(dto.valorParcela());
        conta.setValorTotal(dto.valorTotal());
        conta.setDataVencimento(dto.dataVencimento());
        conta.setStatus(dto.status());

        conta = contasReceberRepository.save(conta);
        return new ContasReceberResponseDTO(conta);
    }

    public List<ContasReceberResponseDTO> listarContasReceber() {
        return contasReceberRepository.findAll().stream()
                .map(ContasReceberResponseDTO::new)
                .toList();
    }

    public ContasReceberResponseDTO buscarContaReceberPorId(Integer id) {
        ContasReceber conta = contasReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada."));
        return new ContasReceberResponseDTO(conta);
    }

    public Page<ContasReceberResponseDTO> buscarContasReceberComFiltro(
            String razaoSocial,
            LocalDate dataInicio,
            LocalDate dataFim,
            Integer idCliente,
            Boolean somenteReceber,
            Pageable pageable) {
        Page<ContasReceber> contasReceber;

        if (idCliente != null) {
            String razaoSocialParam = (razaoSocial != null && !razaoSocial.trim().isEmpty())
                    ? "%" + razaoSocial + "%"
                    : null;
            if (Boolean.TRUE.equals(somenteReceber)) {
                contasReceber = contasReceberRepository.buscarPorClienteComFiltroSomenteReceber(idCliente, razaoSocialParam, dataInicio, dataFim, pageable);
            } else {
                contasReceber = contasReceberRepository.buscarPorClienteComFiltro(idCliente, razaoSocialParam, dataInicio, dataFim, pageable);
            }
        } else if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            if (Boolean.TRUE.equals(somenteReceber)) {
                contasReceber = contasReceberRepository.buscarPorIntervaloDeDatasSomenteReceber(dataInicio, dataFim, pageable);
            } else {
                contasReceber = contasReceberRepository.buscarPorIntervaloDeDatas(dataInicio, dataFim, pageable);
            }
        } else {
            if (Boolean.TRUE.equals(somenteReceber)) {
                contasReceber = contasReceberRepository.buscarPorFiltroSomenteReceber(razaoSocial, dataInicio, dataFim, pageable);
            } else {
                contasReceber = contasReceberRepository.buscarPorFiltro(razaoSocial, dataInicio, dataFim, pageable);
            }
        }

        return contasReceber.map(ContasReceberResponseDTO::new);
    }

    public ContasReceberResponseDTO atualizarContaReceber(Integer id, ContasReceberRequestDTO dto) {
        ContasReceber contaExistente = contasReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada."));

        contaExistente.setCliente(clientesRepository.findById(dto.idCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado.")));
        contaExistente.setFormaPagamento(formaPagamentoRepository.findById(dto.idFormaPagamento())
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada.")));
        contaExistente.setTiposCobranca(tiposCobrancaRepository.findById(dto.idTipoCobranca())
                .orElseThrow(() -> new RuntimeException("Tipo de cobrança não encontrado.")));

        contaExistente.setNumeroDocumento(dto.numeroDocumento());
        contaExistente.setParcela(dto.parcela());
        contaExistente.setValorParcela(dto.valorParcela());
        contaExistente.setValorTotal(dto.valorTotal());
        contaExistente.setDataVencimento(dto.dataVencimento());
        contaExistente.setStatus(dto.status());

        ContasReceber contaAtualizada = contasReceberRepository.save(contaExistente);
        return new ContasReceberResponseDTO(contaAtualizada);
    }

    public void excluirContaReceber(Integer id) {
        if (!contasReceberRepository.existsById(id)) {
            throw new RuntimeException("Conta a receber não encontrada.");
        }
        contasReceberRepository.deleteById(id);
    }

    public List<ContasReceberResponseDTO> filtrarContasReceber(ContasReceberFiltroDTO filtro) {
        List<ContasReceber> lista = contasReceberRepository.filterContasReceber(
                filtro.dataRecebimentoInicio(),
                filtro.dataRecebimentoFim(),
                filtro.idCliente(),
                filtro.idTipoCobranca(),
                filtro.idFormaPagamento(),
                filtro.valorTotalInicial(),
                filtro.valorTotalFinal()
        );
        return lista.stream()
                .map(ContasReceberResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ContasReceberResponseDTO realizarRecebimento(Integer id) {
        ContasReceber conta = contasReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada."));

        if ("paga".equals(conta.getStatus())) {
            throw new RuntimeException("Esta conta já foi paga.");
        }

        conta.setStatus("paga");

        ContasReceber contaAtualizada = contasReceberRepository.save(conta);
        return new ContasReceberResponseDTO(contaAtualizada);
    }
}
