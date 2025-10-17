package com.example.lite_erp.services;

import com.example.lite_erp.entities.clientes.Clientes;
import com.example.lite_erp.entities.clientes.ClientesRepository;
import com.example.lite_erp.entities.pedidos.*;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobranca;
import com.example.lite_erp.entities.tipos_cobranca.TiposCobrancaRepository;
import com.example.lite_erp.entities.vendedores.Vendedores;
import com.example.lite_erp.entities.vendedores.VendedoresRepository;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixa;
import com.example.lite_erp.entities.fluxo_caixa.conta_caixa.ContaCaixaRepository;
import com.example.lite_erp.infra.security.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidosService {

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private VendedoresRepository vendedoresRepository;

    @Autowired
    private TiposCobrancaRepository tiposCobrancaRepository;

    @Autowired
    private FluxoCaixaIntegracaoService fluxoCaixaIntegracaoService;

    @Autowired
    private ContaCaixaRepository contaCaixaRepository;

    @Autowired
    private UsuarioContaCaixaService usuarioContaCaixaService;

    // Método para listar todos os pedidos
    public List<Pedidos> listarTodos() {
        return pedidosRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    // Método para buscar um pedido pelo ID
    public Optional<Pedidos> buscarPorId(Long id) {
        return pedidosRepository.findById(id);
    }

    public Optional<PedidosBuscaResponseDTO> simplesBuscaPorId(Long id) {
        return pedidosRepository.findById(id)
                .map(pedido -> new PedidosBuscaResponseDTO(
                        pedido.getId(),
                        pedido.getCliente() != null ? pedido.getCliente().getRazaoSocial() : pedido.getClienteFinal(),
                        pedido.getVendedor().getNome(),
                        pedido.getDataEmissao(),
                        pedido.getStatus()
                ));
    }

    // Método para criar um novo pedido
    public Pedidos criarPedido(PedidosRequestDTO dto) {
        // Busca as entidades pelos seus IDs
        Clientes cliente = null;
        if (dto.idCliente() != null) {
            cliente = clientesRepository.findById(dto.idCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        }

        Vendedores vendedor = vendedoresRepository.findById(dto.idVendedor())
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        TiposCobranca tipoCobranca = tiposCobrancaRepository.findById(dto.idTipoCobranca())
                .orElseThrow(() -> new RuntimeException("Tipo de cobrança não encontrado"));

        Pedidos pedido = new Pedidos(
                null,
                cliente,
                dto.clienteFinal(),
                vendedor,
                dto.dataEmissao(),
                dto.valorTotal(),
                dto.status(),
                tipoCobranca,
                LocalDateTime.now()  // Define a data de última atualização como o horário atual
        );
        return pedidosRepository.save(pedido);
    }

    // Método para atualizar um pedido existente
    public Optional<Pedidos> atualizarPedido(Long id, PedidosRequestDTO dto) {
        return pedidosRepository.findById(id).map(pedido -> {
            Clientes cliente = null;
            if (dto.idCliente() != null) {
                cliente = clientesRepository.findById(dto.idCliente())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            }

            Vendedores vendedor = vendedoresRepository.findById(dto.idVendedor())
                    .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

            TiposCobranca tipoCobranca = tiposCobrancaRepository.findById(dto.idTipoCobranca())
                    .orElseThrow(() -> new RuntimeException("Tipo de cobrança não encontrado"));

            pedido.setCliente(cliente);
            pedido.setClienteFinal(dto.clienteFinal());
            pedido.setVendedor(vendedor);
            pedido.setDataEmissao(dto.dataEmissao());
            pedido.setValorTotal(dto.valorTotal());
            pedido.setStatus(dto.status());
            pedido.setTipoCobranca(tipoCobranca);

            return pedidosRepository.save(pedido);
        });
    }

    // Método para deletar um pedido
    public boolean deletarPedido(Long id) {
        if (pedidosRepository.existsById(id)) {
            pedidosRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Pedidos> getPedidosEmAberto() {
        return pedidosRepository.findByStatusOrderByIdDesc("em_aberto");
    }

    public Optional<Pedidos> atualizarStatus(Long id, String novoStatus) {
        return pedidosRepository.findById(id).map(pedido -> {
            String statusAnterior = pedido.getStatus();
            pedido.setStatus(novoStatus);
            pedido.setUltimaAtualizacao(LocalDateTime.now());  // Atualiza a data de última atualização

            Pedidos pedidoSalvo = pedidosRepository.save(pedido);  // Salva o pedido com o novo status

            // Integração com fluxo de caixa quando o pedido é baixado
            if ("baixado".equals(novoStatus) && !"baixado".equals(statusAnterior)) {
                try {
                    processarRecebimentoPedido(pedidoSalvo);
                } catch (Exception e) {
                    // Log do erro, mas não falha a operação principal
                    System.err.println("Erro ao processar recebimento do pedido " + id + ": " + e.getMessage());
                }
            }

            return pedidoSalvo;
        });
    }

    public Page<PedidosBuscaResponseDTO> buscarPedidos(Pageable pageable) {
        return pedidosRepository.findPedidosForBusca(pageable);
    }

    public Page<PedidosBuscaResponseDTO> buscarPedidosPorRazaoSocial(String razaoSocial, Pageable pageable) {
        return pedidosRepository.findPedidosForBuscaByClienteRazaoSocial(razaoSocial + "%", pageable);
    }

    public List<PedidosResponseDTO> filtrarPedidos(PedidosFiltroDTO filtro) {
        LocalDate dataInicio = filtro.dataEmissaoInicio();
        LocalDate dataFim    = filtro.dataEmissaoFim();

        LocalDateTime dtInicio = (dataInicio != null)
                ? dataInicio.atStartOfDay()
                : null;

        LocalDateTime dtFim = (dataFim != null)
                ? dataFim.atTime(LocalTime.MAX)
                : null;

        BigDecimal totalInicial = filtro.valorTotalInical();
        BigDecimal totalFinal   = filtro.valorTotalFinal();

        List<Pedidos> lista = pedidosRepository.filterPedidos(
                filtro.idCliente(),
                filtro.idVendedor(),
                dtInicio,
                dtFim,
                totalInicial,
                totalFinal,
                filtro.status(),
                filtro.idTipoCobranca()
        );
        return lista.stream()
                .map(PedidosResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para processar recebimento automático quando pedido é baixado
     */
    private void processarRecebimentoPedido(Pedidos pedido) {
        // Buscar conta de caixa padrão do usuário logado
        Long contaCaixaId = usuarioContaCaixaService.obterContaCaixaPadraoUsuarioLogado();
        Long usuarioLogadoId = AuthenticationUtils.getUsuarioLogadoId();

        // Determinar o nome do cliente
        String nomeCliente = pedido.getCliente() != null
                ? pedido.getCliente().getRazaoSocial()
                : pedido.getClienteFinal();

        // Criar movimentação de entrada via integração
        fluxoCaixaIntegracaoService.processarRecebimentoPedido(
                pedido.getId(),
                "PED-" + pedido.getId(),
                nomeCliente,
                pedido.getValorTotal(),
                contaCaixaId,
                usuarioLogadoId,
                LocalDate.now(),
                "Recebimento automático - Pedido #" + pedido.getId() + " baixado"
        );
    }
}

