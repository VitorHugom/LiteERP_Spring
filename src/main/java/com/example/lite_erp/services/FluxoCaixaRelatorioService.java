package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.*;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixa;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaFiltroDTO;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FluxoCaixaRelatorioService {

    @Autowired
    private MovimentacaoCaixaRepository movimentacaoCaixaRepository;

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    /**
     * Gera resumo do fluxo de caixa por período
     */
    @Transactional(readOnly = true)
    public ResumoFluxoCaixaResponseDTO gerarResumoFluxoCaixa(
            Long contaCaixaId, 
            LocalDate dataInicio, 
            LocalDate dataFim) {

        MovimentacaoCaixaFiltroDTO filtro = new MovimentacaoCaixaFiltroDTO(
                contaCaixaId, null, null, null, null,
                dataInicio, dataFim, null, null,
                MovimentacaoCaixa.StatusMovimentacao.CONFIRMADO,
                null, null
        );

        // Buscar todas as movimentações do período
        var movimentacoes = movimentacaoCaixaRepository.findByFiltros(
                filtro.contaCaixaId(),
                filtro.tipoMovimentacaoId(),
                filtro.categoria(),
                filtro.centroCustoId(),
                filtro.tipoOrigem(),
                filtro.dataInicio(),
                filtro.dataFim(),
                filtro.valorMinimo(),
                filtro.valorMaximo(),
                filtro.status(),
                filtro.usuarioResponsavelId(),
                filtro.descricao(),
                org.springframework.data.domain.Pageable.unpaged()
        );

        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;
        long quantidadeMovimentacoes = movimentacoes.getTotalElements();

        for (var movimentacao : movimentacoes.getContent()) {
            if (movimentacao.getValor().compareTo(BigDecimal.ZERO) > 0) {
                totalReceitas = totalReceitas.add(movimentacao.getValor());
            } else {
                totalDespesas = totalDespesas.add(movimentacao.getValor().abs());
            }
        }

        BigDecimal saldoPeriodo = totalReceitas.subtract(totalDespesas);

        return new ResumoFluxoCaixaResponseDTO(
                dataFim,
                totalReceitas,
                totalDespesas,
                saldoPeriodo,
                quantidadeMovimentacoes
        );
    }

    /**
     * Gera resumo consolidado de todas as contas acessíveis pelo usuário
     */
    @Transactional(readOnly = true)
    public List<ResumoFluxoCaixaResponseDTO> gerarResumoConsolidado(
            Long usuarioId, 
            LocalDate dataInicio, 
            LocalDate dataFim) {

        List<ContaCaixa> contasAcessiveis = contaCaixaRepository.findContasAcessiveisPorUsuario(usuarioId);

        return contasAcessiveis.stream()
                .map(conta -> gerarResumoFluxoCaixa(conta.getId(), dataInicio, dataFim))
                .toList();
    }

    /**
     * Calcula saldo de uma conta em uma data específica
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularSaldoNaData(Long contaCaixaId, LocalDate data) {
        BigDecimal saldo = movimentacaoCaixaRepository.calcularSaldoPorPeriodo(
                contaCaixaId, 
                LocalDate.of(1900, 1, 1), // Data muito antiga para pegar tudo
                data
        );
        return saldo != null ? saldo : BigDecimal.ZERO;
    }

    /**
     * Gera relatório de movimentações por centro de custo
     */
    @Transactional(readOnly = true)
    public List<Object[]> gerarRelatorioPorCentroCusto(
            Long usuarioId, 
            LocalDate dataInicio, 
            LocalDate dataFim) {

        // Esta query seria implementada no repository se necessário
        // Por enquanto, retorna lista vazia
        return List.of();
    }

    /**
     * Gera relatório de movimentações por tipo
     */
    @Transactional(readOnly = true)
    public List<Object[]> gerarRelatorioPorTipo(
            Long usuarioId, 
            LocalDate dataInicio, 
            LocalDate dataFim) {

        // Esta query seria implementada no repository se necessário
        // Por enquanto, retorna lista vazia
        return List.of();
    }

    /**
     * Calcula projeção de fluxo baseado em contas em aberto
     */
    @Transactional(readOnly = true)
    public BigDecimal calcularProjecaoFluxo(Long contaCaixaId, LocalDate dataLimite) {
        // Implementação futura: buscar contas a pagar e receber em aberto
        // e calcular projeção de entradas e saídas
        return BigDecimal.ZERO;
    }
}
