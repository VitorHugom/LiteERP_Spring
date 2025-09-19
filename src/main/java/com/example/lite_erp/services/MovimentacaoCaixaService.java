package com.example.lite_erp.services;

import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCusto;
import com.example.lite_erp.entities.fluxo_caixa.centro_custo.CentroCustoRepository;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.*;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoRepository;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.entities.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MovimentacaoCaixaService {

    @Autowired
    private MovimentacaoCaixaRepository movimentacaoCaixaRepository;

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    @Autowired
    private TipoMovimentacaoRepository tipoMovimentacaoRepository;

    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaCaixaService contaCaixaService;

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> listarTodas(Pageable pageable) {
        return movimentacaoCaixaRepository.findAll(pageable)
                .map(MovimentacaoCaixaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> listarTodas(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (dataInicio != null || dataFim != null) {
            MovimentacaoCaixaFiltroDTO filtro = new MovimentacaoCaixaFiltroDTO(
                    null, null, null, null, null,
                    dataInicio, dataFim, null, null,
                    null, null, ""
            );
            return filtrar(filtro, pageable);
        }
        return listarTodas(pageable);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> listarPorConta(Long contaCaixaId, Pageable pageable) {
        return movimentacaoCaixaRepository.findByContaCaixaIdOrderByDataMovimentacaoDescDataLancamentoDesc(contaCaixaId, pageable)
                .map(MovimentacaoCaixaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> listarPorConta(Long contaCaixaId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (dataInicio != null && dataFim != null) {
            // Ambas as datas informadas
            return movimentacaoCaixaRepository.findByContaCaixaIdAndDataMovimentacaoBetween(contaCaixaId, dataInicio, dataFim, pageable)
                    .map(MovimentacaoCaixaResponseDTO::new);
        } else if (dataInicio != null) {
            // Apenas data início informada
            return movimentacaoCaixaRepository.findByContaCaixaIdAndDataMovimentacaoGreaterThanEqual(contaCaixaId, dataInicio, pageable)
                    .map(MovimentacaoCaixaResponseDTO::new);
        } else if (dataFim != null) {
            // Apenas data fim informada
            return movimentacaoCaixaRepository.findByContaCaixaIdAndDataMovimentacaoLessThanEqual(contaCaixaId, dataFim, pageable)
                    .map(MovimentacaoCaixaResponseDTO::new);
        }
        return listarPorConta(contaCaixaId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> listarAcessiveisPorUsuario(Long usuarioId, Pageable pageable) {
        return movimentacaoCaixaRepository.findMovimentacoesAcessiveisPorUsuario(usuarioId, pageable)
                .map(MovimentacaoCaixaResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> listarAcessiveisPorUsuario(Long usuarioId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (dataInicio != null || dataFim != null) {
            return movimentacaoCaixaRepository.findMovimentacoesAcessiveisPorUsuarioComFiltroData(usuarioId, dataInicio, dataFim, pageable)
                    .map(MovimentacaoCaixaResponseDTO::new);
        }
        return listarAcessiveisPorUsuario(usuarioId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<MovimentacaoCaixaResponseDTO> buscarPorId(Long id) {
        return movimentacaoCaixaRepository.findById(id)
                .map(MovimentacaoCaixaResponseDTO::new);
    }

    @Transactional
    public MovimentacaoCaixaResponseDTO salvar(MovimentacaoCaixaRequestDTO dto, Long usuarioLancamentoId) {
        ContaCaixa conta = contaCaixaRepository.findById(dto.contaCaixaId())
                .orElseThrow(() -> new RuntimeException("Conta de caixa não encontrada"));

        TipoMovimentacao tipo = tipoMovimentacaoRepository.findById(dto.tipoMovimentacaoId())
                .orElseThrow(() -> new RuntimeException("Tipo de movimentação não encontrado"));

        Usuario usuarioLancamento = usuarioRepository.findById(usuarioLancamentoId.toString())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        CentroCusto centroCusto = null;
        if (dto.centroCustoId() != null) {
            centroCusto = centroCustoRepository.findById(dto.centroCustoId())
                    .orElseThrow(() -> new RuntimeException("Centro de custo não encontrado"));
        }

        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
        movimentacao.setContaCaixa(conta);
        movimentacao.setTipoMovimentacao(tipo);
        movimentacao.setCentroCusto(centroCusto);
        movimentacao.setTipoOrigem(MovimentacaoCaixa.TipoOrigem.MANUAL);
        movimentacao.setNumeroDocumento(dto.numeroDocumento());
        movimentacao.setDescricao(dto.descricao());
        movimentacao.setValor(dto.valor());
        movimentacao.setDataMovimentacao(dto.dataMovimentacao());
        movimentacao.setUsuarioLancamento(usuarioLancamento);
        movimentacao.setObservacoes(dto.observacoes());
        movimentacao.setStatus(MovimentacaoCaixa.StatusMovimentacao.CONFIRMADO);

        MovimentacaoCaixa movimentacaoSalva = movimentacaoCaixaRepository.save(movimentacao);
        
        // Atualizar saldo da conta
        contaCaixaService.atualizarSaldo(conta.getId());

        return new MovimentacaoCaixaResponseDTO(movimentacaoSalva);
    }

    @Transactional
    public MovimentacaoCaixaResponseDTO criarMovimentacaoAutomatica(
            Long contaCaixaId,
            Long tipoMovimentacaoId,
            MovimentacaoCaixa.TipoOrigem tipoOrigem,
            Long referenciaId,
            String numeroDocumento,
            String descricao,
            BigDecimal valor,
            LocalDate dataMovimentacao,
            Long usuarioLancamentoId,
            String observacoes) {

        ContaCaixa conta = contaCaixaRepository.findById(contaCaixaId)
                .orElseThrow(() -> new RuntimeException("Conta de caixa não encontrada"));

        TipoMovimentacao tipo = tipoMovimentacaoRepository.findById(tipoMovimentacaoId)
                .orElseThrow(() -> new RuntimeException("Tipo de movimentação não encontrado"));

        Usuario usuarioLancamento = usuarioRepository.findById(usuarioLancamentoId.toString())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa();
        movimentacao.setContaCaixa(conta);
        movimentacao.setTipoMovimentacao(tipo);
        movimentacao.setTipoOrigem(tipoOrigem);
        movimentacao.setReferenciaId(referenciaId);
        movimentacao.setNumeroDocumento(numeroDocumento);
        movimentacao.setDescricao(descricao);
        movimentacao.setValor(valor);
        movimentacao.setDataMovimentacao(dataMovimentacao);
        movimentacao.setUsuarioLancamento(usuarioLancamento);
        movimentacao.setObservacoes(observacoes);
        movimentacao.setStatus(MovimentacaoCaixa.StatusMovimentacao.CONFIRMADO);

        MovimentacaoCaixa movimentacaoSalva = movimentacaoCaixaRepository.save(movimentacao);
        
        // Atualizar saldo da conta
        contaCaixaService.atualizarSaldo(conta.getId());

        return new MovimentacaoCaixaResponseDTO(movimentacaoSalva);
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoCaixaResponseDTO> filtrar(MovimentacaoCaixaFiltroDTO filtro, Pageable pageable) {
        // Garantir que descricao nunca seja null para evitar problemas com PostgreSQL
        String descricao = filtro.descricao() != null ? filtro.descricao() : "";

        return movimentacaoCaixaRepository.findByFiltros(
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
                descricao,
                pageable
        ).map(MovimentacaoCaixaResponseDTO::new);
    }

    @Transactional
    public boolean cancelar(Long id) {
        return movimentacaoCaixaRepository.findById(id)
                .map(movimentacao -> {
                    movimentacao.setStatus(MovimentacaoCaixa.StatusMovimentacao.CANCELADO);
                    movimentacaoCaixaRepository.save(movimentacao);
                    
                    // Atualizar saldo da conta
                    contaCaixaService.atualizarSaldo(movimentacao.getContaCaixa().getId());
                    
                    return true;
                })
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoCaixaResponseDTO> buscarPorReferencia(MovimentacaoCaixa.TipoOrigem tipoOrigem, Long referenciaId) {
        return movimentacaoCaixaRepository.findByTipoOrigemAndReferenciaId(tipoOrigem, referenciaId)
                .stream()
                .map(MovimentacaoCaixaResponseDTO::new)
                .toList();
    }
}
