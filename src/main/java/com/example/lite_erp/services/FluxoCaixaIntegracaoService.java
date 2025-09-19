package com.example.lite_erp.services;

import com.example.lite_erp.entities.contas_pagar.ContasPagar;
import com.example.lite_erp.entities.contas_pagar.ContasPagarRepository;
import com.example.lite_erp.entities.contas_receber.ContasReceber;
import com.example.lite_erp.entities.contas_receber.ContasReceberRepository;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixa;
import com.example.lite_erp.entities.fluxo_caixa.movimentacao_caixa.MovimentacaoCaixaResponseDTO;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacao;
import com.example.lite_erp.entities.fluxo_caixa.tipo_movimentacao.TipoMovimentacaoRepository;
import com.example.lite_erp.infra.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FluxoCaixaIntegracaoService {

    @Autowired
    private MovimentacaoCaixaService movimentacaoCaixaService;

    @Autowired
    private ContasPagarRepository contasPagarRepository;

    @Autowired
    private ContasReceberRepository contasReceberRepository;

    @Autowired
    private TipoMovimentacaoRepository tipoMovimentacaoRepository;

    @Autowired
    private UsuarioContaCaixaService usuarioContaCaixaService;

    /**
     * Processa o pagamento de uma conta a pagar, criando a movimentação no caixa
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarPagamentoContaPagar(
            Long contaPagarId, 
            Long contaCaixaId, 
            Long usuarioLancamentoId,
            LocalDate dataPagamento,
            String observacoes) {

        ContasPagar contaPagar = contasPagarRepository.findById(contaPagarId)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        if (!"aberta".equals(contaPagar.getStatus())) {
            throw new RuntimeException("Conta a pagar já foi paga ou cancelada");
        }

        // Buscar tipo de movimentação para pagamento de fornecedor
        TipoMovimentacao tipoPagamento = tipoMovimentacaoRepository
                .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.DESPESA)
                .stream()
                .filter(t -> t.getDescricao().toLowerCase().contains("fornecedor"))
                .findFirst()
                .orElse(tipoMovimentacaoRepository
                        .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.DESPESA)
                        .get(0));

        // Criar movimentação de saída (valor negativo)
        MovimentacaoCaixaResponseDTO movimentacao = movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoPagamento.getId(),
                MovimentacaoCaixa.TipoOrigem.CONTA_PAGAR,
                contaPagar.getId(),
                contaPagar.getNumeroDocumento(),
                "Pagamento - " + contaPagar.getFornecedor().getRazaoSocial(),
                contaPagar.getValorParcela().negate(), // Valor negativo para saída
                dataPagamento,
                usuarioLancamentoId,
                observacoes
        );

        // Atualizar status da conta a pagar
        contaPagar.setStatus("paga");
        contasPagarRepository.save(contaPagar);

        return movimentacao;
    }

    /**
     * Processa o recebimento de uma conta a receber, criando a movimentação no caixa
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarRecebimentoContaReceber(
            Integer contaReceberId, 
            Long contaCaixaId, 
            Long usuarioLancamentoId,
            LocalDate dataRecebimento,
            String observacoes) {

        ContasReceber contaReceber = contasReceberRepository.findById(contaReceberId)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada"));

        if (!"aberta".equals(contaReceber.getStatus())) {
            throw new RuntimeException("Conta a receber já foi paga ou cancelada");
        }

        // Buscar tipo de movimentação para recebimento de cliente
        TipoMovimentacao tipoRecebimento = tipoMovimentacaoRepository
                .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.RECEITA)
                .stream()
                .filter(t -> t.getDescricao().toLowerCase().contains("cliente"))
                .findFirst()
                .orElse(tipoMovimentacaoRepository
                        .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.RECEITA)
                        .get(0));

        // Criar movimentação de entrada (valor positivo)
        MovimentacaoCaixaResponseDTO movimentacao = movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoRecebimento.getId(),
                MovimentacaoCaixa.TipoOrigem.CONTA_RECEBER,
                contaReceber.getId().longValue(),
                contaReceber.getNumeroDocumento(),
                "Recebimento - " + contaReceber.getCliente().getRazaoSocial(),
                contaReceber.getValorParcela(), // Valor positivo para entrada
                dataRecebimento,
                usuarioLancamentoId,
                observacoes
        );

        // Atualizar status da conta a receber
        contaReceber.setStatus("paga");
        contasReceberRepository.save(contaReceber);

        return movimentacao;
    }

    /**
     * Cancela um pagamento/recebimento, revertendo a movimentação e o status da conta
     */
    @Transactional
    public boolean cancelarMovimentacaoIntegrada(Long movimentacaoId) {
        return movimentacaoCaixaService.buscarPorId(movimentacaoId)
                .map(movimentacao -> {
                    if (movimentacao.tipoOrigem() == MovimentacaoCaixa.TipoOrigem.CONTA_PAGAR) {
                        // Reverter status da conta a pagar
                        contasPagarRepository.findById(movimentacao.referenciaId())
                                .ifPresent(conta -> {
                                    conta.setStatus("aberta");
                                    contasPagarRepository.save(conta);
                                });
                    } else if (movimentacao.tipoOrigem() == MovimentacaoCaixa.TipoOrigem.CONTA_RECEBER) {
                        // Reverter status da conta a receber
                        contasReceberRepository.findById(movimentacao.referenciaId().intValue())
                                .ifPresent(conta -> {
                                    conta.setStatus("aberta");
                                    contasReceberRepository.save(conta);
                                });
                    }

                    // Cancelar a movimentação
                    return movimentacaoCaixaService.cancelar(movimentacaoId);
                })
                .orElse(false);
    }

    /**
     * Busca movimentações relacionadas a uma conta a pagar
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoCaixaResponseDTO> buscarMovimentacoesContaPagar(Long contaPagarId) {
        return movimentacaoCaixaService.buscarPorReferencia(
                MovimentacaoCaixa.TipoOrigem.CONTA_PAGAR, 
                contaPagarId
        );
    }

    /**
     * Busca movimentações relacionadas a uma conta a receber
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoCaixaResponseDTO> buscarMovimentacoesContaReceber(Integer contaReceberId) {
        return movimentacaoCaixaService.buscarPorReferencia(
                MovimentacaoCaixa.TipoOrigem.CONTA_RECEBER,
                contaReceberId.longValue()
        );
    }

    /**
     * Processa recebimento automático sem verificar status (para integração interna)
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarRecebimentoAutomatico(
            Integer contaReceberId,
            Long contaCaixaId,
            Long usuarioLancamentoId,
            LocalDate dataRecebimento,
            String observacoes) {

        ContasReceber contaReceber = contasReceberRepository.findById(contaReceberId)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada"));

        // Buscar tipo de movimentação para recebimento de cliente
        TipoMovimentacao tipoRecebimento = tipoMovimentacaoRepository
                .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.RECEITA)
                .stream()
                .filter(t -> t.getDescricao().toLowerCase().contains("cliente"))
                .findFirst()
                .orElse(tipoMovimentacaoRepository
                        .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.RECEITA)
                        .get(0));

        // Criar movimentação de entrada (valor positivo)
        return movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoRecebimento.getId(),
                MovimentacaoCaixa.TipoOrigem.CONTA_RECEBER,
                contaReceber.getId().longValue(),
                contaReceber.getNumeroDocumento(),
                "Recebimento - " + contaReceber.getCliente().getRazaoSocial(),
                contaReceber.getValorParcela(), // Valor positivo para entrada
                dataRecebimento,
                usuarioLancamentoId,
                observacoes
        );
    }

    /**
     * Processa pagamento automático sem verificar status (para integração interna)
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarPagamentoAutomatico(
            Long contaPagarId,
            Long contaCaixaId,
            Long usuarioLancamentoId,
            LocalDate dataPagamento,
            String observacoes) {

        ContasPagar contaPagar = contasPagarRepository.findById(contaPagarId)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        // Buscar tipo de movimentação para pagamento de fornecedor
        TipoMovimentacao tipoPagamento = tipoMovimentacaoRepository
                .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.DESPESA)
                .stream()
                .filter(t -> t.getDescricao().toLowerCase().contains("fornecedor"))
                .findFirst()
                .orElse(tipoMovimentacaoRepository
                        .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.DESPESA)
                        .get(0));

        // Criar movimentação de saída (valor negativo)
        return movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoPagamento.getId(),
                MovimentacaoCaixa.TipoOrigem.CONTA_PAGAR,
                contaPagar.getId(),
                contaPagar.getNumeroDocumento(),
                "Pagamento - " + contaPagar.getFornecedor().getRazaoSocial(),
                contaPagar.getValorParcela().negate(), // Valor negativo para saída
                dataPagamento,
                usuarioLancamentoId,
                observacoes
        );
    }

    /**
     * Cria movimentação de estorno para conta a receber (saída - estorno do recebimento)
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO criarEstornoContaReceber(
            Integer contaReceberId,
            Long contaCaixaId,
            Long usuarioLancamentoId,
            LocalDate dataEstorno,
            String observacoes) {

        ContasReceber contaReceber = contasReceberRepository.findById(contaReceberId)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada"));

        // Buscar tipo de movimentação para estorno (despesa)
        TipoMovimentacao tipoEstorno = tipoMovimentacaoRepository
                .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.DESPESA)
                .stream()
                .filter(t -> t.getDescricao().toLowerCase().contains("estorno") ||
                           t.getDescricao().toLowerCase().contains("devolução"))
                .findFirst()
                .orElse(tipoMovimentacaoRepository
                        .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.DESPESA)
                        .get(0));

        // Criar movimentação de saída (valor negativo - estorno do recebimento)
        return movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoEstorno.getId(),
                MovimentacaoCaixa.TipoOrigem.CONTA_RECEBER,
                contaReceber.getId().longValue(),
                contaReceber.getNumeroDocumento(),
                "Estorno Recebimento - " + contaReceber.getCliente().getRazaoSocial(),
                contaReceber.getValorParcela().negate(), // Valor negativo para saída (estorno)
                dataEstorno,
                usuarioLancamentoId,
                observacoes
        );
    }

    /**
     * Cria movimentação de estorno para conta a pagar (entrada - estorno do pagamento)
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO criarEstornoContaPagar(
            Long contaPagarId,
            Long contaCaixaId,
            Long usuarioLancamentoId,
            LocalDate dataEstorno,
            String observacoes) {

        ContasPagar contaPagar = contasPagarRepository.findById(contaPagarId)
                .orElseThrow(() -> new RuntimeException("Conta a pagar não encontrada"));

        // Buscar tipo de movimentação para estorno (receita)
        TipoMovimentacao tipoEstorno = tipoMovimentacaoRepository
                .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.RECEITA)
                .stream()
                .filter(t -> t.getDescricao().toLowerCase().contains("estorno") ||
                           t.getDescricao().toLowerCase().contains("devolução"))
                .findFirst()
                .orElse(tipoMovimentacaoRepository
                        .findByCategoriaAndAtivoTrueOrderByDescricao(TipoMovimentacao.CategoriaMovimentacao.RECEITA)
                        .get(0));

        // Criar movimentação de entrada (valor positivo - estorno do pagamento)
        return movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoEstorno.getId(),
                MovimentacaoCaixa.TipoOrigem.CONTA_PAGAR,
                contaPagar.getId(),
                contaPagar.getNumeroDocumento(),
                "Estorno Pagamento - " + contaPagar.getFornecedor().getRazaoSocial(),
                contaPagar.getValorParcela(), // Valor positivo para entrada (estorno)
                dataEstorno,
                usuarioLancamentoId,
                observacoes
        );
    }

    @Transactional
    public MovimentacaoCaixaResponseDTO processarRecebimentoPedido(
            Long pedidoId,
            String numeroDocumento,
            String descricaoCliente,
            BigDecimal valorRecebimento,
            Long contaCaixaId,
            Long usuarioLancamentoId,
            LocalDate dataRecebimento,
            String observacoes) {

        // Buscar tipo de movimentação para recebimento
        TipoMovimentacao tipoRecebimento = tipoMovimentacaoRepository
                .findByDescricaoContainingIgnoreCase("Venda à Vista")
                .orElse(tipoMovimentacaoRepository
                        .findByDescricaoContainingIgnoreCase("Recebimento")
                        .orElseThrow(() -> new RuntimeException("Tipo de movimentação para recebimento não encontrado")));

        // Criar movimentação de entrada (valor positivo)
        return movimentacaoCaixaService.criarMovimentacaoAutomatica(
                contaCaixaId,
                tipoRecebimento.getId(),
                MovimentacaoCaixa.TipoOrigem.MANUAL, // Usar MANUAL para recebimentos diretos de pedidos
                pedidoId,
                numeroDocumento,
                "Recebimento Pedido - " + descricaoCliente,
                valorRecebimento, // Valor positivo para entrada
                dataRecebimento,
                usuarioLancamentoId,
                observacoes
        );
    }

    // ========== MÉTODOS AUXILIARES PARA USAR CONTA DO USUÁRIO LOGADO ==========

    /**
     * Processa pagamento usando a conta padrão do usuário logado
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarPagamentoContaPagarUsuarioLogado(
            Long contaPagarId,
            LocalDate dataPagamento,
            String observacoes) {

        Long contaCaixaId = usuarioContaCaixaService.obterContaCaixaPadraoUsuarioLogado();
        Long usuarioLogadoId = AuthenticationUtils.getUsuarioLogadoId();

        return processarPagamentoContaPagar(contaPagarId, contaCaixaId, usuarioLogadoId, dataPagamento, observacoes);
    }

    /**
     * Processa recebimento usando a conta padrão do usuário logado
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarRecebimentoContaReceberUsuarioLogado(
            Integer contaReceberId,
            LocalDate dataRecebimento,
            String observacoes) {

        Long contaCaixaId = usuarioContaCaixaService.obterContaCaixaPadraoUsuarioLogado();
        Long usuarioLogadoId = AuthenticationUtils.getUsuarioLogadoId();

        return processarRecebimentoContaReceber(contaReceberId, contaCaixaId, usuarioLogadoId, dataRecebimento, observacoes);
    }

    /**
     * Processa recebimento de pedido usando a conta padrão do usuário logado
     */
    @Transactional
    public MovimentacaoCaixaResponseDTO processarRecebimentoPedidoUsuarioLogado(
            Long pedidoId,
            String numeroDocumento,
            String descricaoCliente,
            BigDecimal valorRecebimento,
            LocalDate dataRecebimento,
            String observacoes) {

        Long contaCaixaId = usuarioContaCaixaService.obterContaCaixaPadraoUsuarioLogado();
        Long usuarioLogadoId = AuthenticationUtils.getUsuarioLogadoId();

        return processarRecebimentoPedido(pedidoId, numeroDocumento, descricaoCliente, valorRecebimento,
                contaCaixaId, usuarioLogadoId, dataRecebimento, observacoes);
    }
}
