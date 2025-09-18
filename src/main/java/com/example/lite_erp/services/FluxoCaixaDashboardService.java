package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.*;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixa;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaFiltroDTO;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaRepository;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FluxoCaixaDashboardService {

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    @Autowired
    private MovimentacaoCaixaRepository movimentacaoCaixaRepository;

    @Autowired
    private FluxoCaixaRelatorioService relatorioService;

    @Autowired
    private MovimentacaoCaixaService movimentacaoCaixaService;

    @Transactional(readOnly = true)
    public DashboardFluxoCaixaResponseDTO gerarDashboard(Long usuarioId) {
        LocalDate hoje = LocalDate.now();
        YearMonth mesAtual = YearMonth.from(hoje);
        YearMonth mesAnterior = mesAtual.minusMonths(1);

        // Buscar contas acessíveis
        List<ContaCaixa> contasAcessiveis = contaCaixaRepository.findContasAcessiveisPorUsuario(usuarioId);

        // Calcular saldo total atual
        BigDecimal saldoTotalAtual = contasAcessiveis.stream()
                .map(ContaCaixa::getSaldoAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular totais do mês atual
        var totaisMesAtual = calcularTotaisPorPeriodo(contasAcessiveis, 
                mesAtual.atDay(1), mesAtual.atEndOfMonth());

        // Calcular totais do mês anterior
        var totaisMesAnterior = calcularTotaisPorPeriodo(contasAcessiveis, 
                mesAnterior.atDay(1), mesAnterior.atEndOfMonth());

        // Calcular percentuais de variação
        BigDecimal percentualVariacaoReceitas = calcularPercentualVariacao(
                totaisMesAnterior.receitas(), totaisMesAtual.receitas());
        BigDecimal percentualVariacaoDespesas = calcularPercentualVariacao(
                totaisMesAnterior.despesas(), totaisMesAtual.despesas());

        // Buscar conta com maior saldo
        DashboardFluxoCaixaResponseDTO.ContaResumoDTO contaMaiorSaldo = contasAcessiveis.stream()
                .max(Comparator.comparing(ContaCaixa::getSaldoAtual))
                .map(conta -> new DashboardFluxoCaixaResponseDTO.ContaResumoDTO(
                        conta.getId(),
                        conta.getDescricao(),
                        conta.getSaldoAtual(),
                        conta.getTipo()
                ))
                .orElse(null);

        // Buscar últimas movimentações
        List<DashboardFluxoCaixaResponseDTO.MovimentacaoResumoDTO> ultimasMovimentacoes = 
                buscarUltimasMovimentacoes(usuarioId, 5);

        // Gerar resumo por categoria
        List<DashboardFluxoCaixaResponseDTO.CategoriaResumoDTO> resumoPorCategoria = 
                gerarResumoPorCategoria(contasAcessiveis, mesAtual.atDay(1), mesAtual.atEndOfMonth());

        return new DashboardFluxoCaixaResponseDTO(
                hoje,
                saldoTotalAtual,
                totaisMesAtual.receitas(),
                totaisMesAtual.despesas(),
                totaisMesAtual.saldo(),
                totaisMesAnterior.receitas(),
                totaisMesAnterior.despesas(),
                percentualVariacaoReceitas,
                percentualVariacaoDespesas,
                totaisMesAtual.quantidade(),
                contaMaiorSaldo,
                ultimasMovimentacoes,
                resumoPorCategoria
        );
    }

    private record TotaisPeriodo(BigDecimal receitas, BigDecimal despesas, BigDecimal saldo, Long quantidade) {}

    private TotaisPeriodo calcularTotaisPorPeriodo(List<ContaCaixa> contas, LocalDate inicio, LocalDate fim) {
        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;
        long totalQuantidade = 0;

        for (ContaCaixa conta : contas) {
            MovimentacaoCaixaFiltroDTO filtro = new MovimentacaoCaixaFiltroDTO(
                    conta.getId(), null, null, null, null,
                    inicio, fim, null, null,
                    MovimentacaoCaixa.StatusMovimentacao.CONFIRMADO,
                    null, null
            );

            var movimentacoes = movimentacaoCaixaRepository.findByFiltros(
                    filtro.contaCaixaId(), filtro.tipoMovimentacaoId(), filtro.categoria(),
                    filtro.centroCustoId(), filtro.tipoOrigem(), filtro.dataInicio(),
                    filtro.dataFim(), filtro.valorMinimo(), filtro.valorMaximo(),
                    filtro.status(), filtro.usuarioResponsavelId(), filtro.descricao(),
                    PageRequest.of(0, Integer.MAX_VALUE)
            );

            for (var mov : movimentacoes.getContent()) {
                if (mov.getValor().compareTo(BigDecimal.ZERO) > 0) {
                    totalReceitas = totalReceitas.add(mov.getValor());
                } else {
                    totalDespesas = totalDespesas.add(mov.getValor().abs());
                }
                totalQuantidade++;
            }
        }

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);
        return new TotaisPeriodo(totalReceitas, totalDespesas, saldo, totalQuantidade);
    }

    private BigDecimal calcularPercentualVariacao(BigDecimal valorAnterior, BigDecimal valorAtual) {
        if (valorAnterior.compareTo(BigDecimal.ZERO) == 0) {
            return valorAtual.compareTo(BigDecimal.ZERO) > 0 ? new BigDecimal("100") : BigDecimal.ZERO;
        }

        return valorAtual.subtract(valorAnterior)
                .divide(valorAnterior, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private List<DashboardFluxoCaixaResponseDTO.MovimentacaoResumoDTO> buscarUltimasMovimentacoes(
            Long usuarioId, int limite) {
        
        var movimentacoes = movimentacaoCaixaService.listarAcessiveisPorUsuario(
                usuarioId, PageRequest.of(0, limite));

        return movimentacoes.getContent().stream()
                .map(mov -> new DashboardFluxoCaixaResponseDTO.MovimentacaoResumoDTO(
                        mov.id(),
                        mov.descricao(),
                        mov.valor(),
                        mov.dataMovimentacao(),
                        mov.categoria(),
                        mov.contaCaixaDescricao()
                ))
                .toList();
    }

    private List<DashboardFluxoCaixaResponseDTO.CategoriaResumoDTO> gerarResumoPorCategoria(
            List<ContaCaixa> contas, LocalDate inicio, LocalDate fim) {
        
        Map<TipoMovimentacao.CategoriaMovimentacao, List<MovimentacaoCaixa>> movimentacoesPorCategoria =
                contas.stream()
                .flatMap(conta -> {
                    MovimentacaoCaixaFiltroDTO filtro = new MovimentacaoCaixaFiltroDTO(
                            conta.getId(), null, null, null, null,
                            inicio, fim, null, null,
                            MovimentacaoCaixa.StatusMovimentacao.CONFIRMADO,
                            null, null
                    );

                    return movimentacaoCaixaRepository.findByFiltros(
                            filtro.contaCaixaId(), filtro.tipoMovimentacaoId(), filtro.categoria(),
                            filtro.centroCustoId(), filtro.tipoOrigem(), filtro.dataInicio(),
                            filtro.dataFim(), filtro.valorMinimo(), filtro.valorMaximo(),
                            filtro.status(), filtro.usuarioResponsavelId(), filtro.descricao(),
                            PageRequest.of(0, Integer.MAX_VALUE)
                    ).getContent().stream();
                })
                .collect(Collectors.groupingBy(mov -> mov.getTipoMovimentacao().getCategoria()));

        BigDecimal totalGeral = movimentacoesPorCategoria.values().stream()
                .flatMap(List::stream)
                .map(mov -> mov.getValor().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return movimentacoesPorCategoria.entrySet().stream()
                .map(entry -> {
                    TipoMovimentacao.CategoriaMovimentacao categoria = entry.getKey();
                    List<MovimentacaoCaixa> movimentacoes = entry.getValue();
                    
                    BigDecimal total = movimentacoes.stream()
                            .map(mov -> mov.getValor().abs())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
                    long quantidade = movimentacoes.size();
                    
                    BigDecimal percentual = totalGeral.compareTo(BigDecimal.ZERO) > 0 
                            ? total.divide(totalGeral, 4, RoundingMode.HALF_UP)
                                   .multiply(new BigDecimal("100"))
                                   .setScale(2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return new DashboardFluxoCaixaResponseDTO.CategoriaResumoDTO(
                            categoria, total, quantidade, percentual);
                })
                .sorted(Comparator.comparing(DashboardFluxoCaixaResponseDTO.CategoriaResumoDTO::total).reversed())
                .toList();
    }
}
