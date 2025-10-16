package com.example.lite_erp.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.lite_erp.entities.recuperacao_senha.*;
import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.entities.usuario.UsuarioRepository;
import com.example.lite_erp.infra.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Service para gerenciar recuperação de senha
 */
@Service
public class RecuperacaoSenhaService {
    
    private static final Logger logger = LoggerFactory.getLogger(RecuperacaoSenhaService.class);
    private static final int EXPIRACAO_MINUTOS = 15;
    private static final int TAMANHO_CODIGO = 6;
    private static final int MAX_TENTATIVAS = 3;
    
    @Autowired
    private RecuperacaoSenhaRepository recuperacaoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${api.security.token.secret}")
    private String secret;
    
    /**
     * Solicita recuperação de senha - Gera código e envia por email
     */
    @Transactional
    public RecuperacaoSenhaResponseDTO solicitarRecuperacao(String nomeUsuario, HttpServletRequest request) {
        logger.info("Solicitação de recuperação de senha para usuário: {}", nomeUsuario);
        
        // Buscar usuário
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
            .orElseThrow(() -> {
                logger.warn("Tentativa de recuperação para usuário inexistente: {}", nomeUsuario);
                // Por segurança, não informar que o usuário não existe
                throw new BusinessException("Se o usuário existir, um código será enviado para o email cadastrado.");
            });
        
        // Verificar se usuário tem email
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            logger.error("Usuário {} não possui email cadastrado", nomeUsuario);
            throw new BusinessException("Usuário não possui email cadastrado. Entre em contato com o suporte.");
        }
        
        // Invalidar códigos anteriores não usados
        invalidarCodigosAnteriores(usuario.getId());
        
        // Gerar código de 6 dígitos
        String codigo = gerarCodigoAleatorio();
        
        // Obter IP da solicitação
        String ip = obterIpRequisicao(request);
        
        // Criar registro de recuperação
        RecuperacaoSenha recuperacao = new RecuperacaoSenha();
        recuperacao.setUsuario(usuario);
        recuperacao.setCodigo(codigo);
        recuperacao.setDataCriacao(LocalDateTime.now());
        recuperacao.setDataExpiracao(LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS));
        recuperacao.setTentativasValidacao(0);
        recuperacao.setValidado(false);
        recuperacao.setUsado(false);
        recuperacao.setIpSolicitacao(ip);
        
        recuperacaoRepository.save(recuperacao);
        
        // Enviar email com código
        try {
            emailService.enviarEmailCodigoRecuperacao(usuario.getEmail(), usuario.getNomeUsuario(), codigo, EXPIRACAO_MINUTOS);
            logger.info("Código de recuperação enviado para o email de {}", nomeUsuario);
        } catch (Exception e) {
            logger.error("Erro ao enviar email de recuperação para {}", nomeUsuario, e);
            throw new BusinessException("Erro ao enviar email. Tente novamente mais tarde.");
        }
        
        // Retornar resposta com email mascarado
        String emailMascarado = mascararEmail(usuario.getEmail());
        return new RecuperacaoSenhaResponseDTO(
            "Código enviado para o email cadastrado",
            emailMascarado,
            EXPIRACAO_MINUTOS
        );
    }
    
    /**
     * Valida código de recuperação - Retorna token temporário
     */
    @Transactional
    public RecuperacaoSenhaResponseDTO validarCodigo(String nomeUsuario, String codigo) {
        logger.info("Validação de código para usuário: {}", nomeUsuario);
        
        // Buscar usuário
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
            .orElseThrow(() -> new BusinessException("Código inválido ou expirado"));
        
        // Buscar código de recuperação válido
        RecuperacaoSenha recuperacao = recuperacaoRepository.findCodigoValido(
            usuario.getId(),
            codigo,
            LocalDateTime.now()
        ).orElseThrow(() -> {
            logger.warn("Código inválido ou expirado para usuário: {}", nomeUsuario);
            return new BusinessException("Código inválido ou expirado");
        });
        
        // Verificar se pode ser validado
        if (!recuperacao.podeSerValidado()) {
            logger.warn("Código não pode ser validado para usuário: {}", nomeUsuario);
            
            if (recuperacao.atingiuLimiteTentativas()) {
                throw new BusinessException("Limite de tentativas excedido. Solicite um novo código.");
            }
            if (recuperacao.isExpirado()) {
                throw new BusinessException("Código expirado. Solicite um novo código.");
            }
            if (recuperacao.getValidado()) {
                throw new BusinessException("Código já foi validado.");
            }
            if (recuperacao.getUsado()) {
                throw new BusinessException("Código já foi utilizado.");
            }
            
            throw new BusinessException("Código inválido");
        }
        
        // Verificar se o código está correto
        if (!recuperacao.getCodigo().equals(codigo)) {
            recuperacao.incrementarTentativas();
            recuperacaoRepository.save(recuperacao);
            
            int tentativasRestantes = MAX_TENTATIVAS - recuperacao.getTentativasValidacao();
            logger.warn("Código incorreto para usuário: {}. Tentativas restantes: {}", nomeUsuario, tentativasRestantes);
            
            throw new BusinessException(
                String.format("Código incorreto. Você tem mais %d tentativa(s)", tentativasRestantes)
            );
        }
        
        // Gerar token temporário
        String tokenTemporario = gerarTokenTemporario(usuario, recuperacao.getId());
        
        // Marcar como validado e salvar token
        recuperacao.setValidado(true);
        recuperacao.setTokenTemporario(tokenTemporario);
        recuperacaoRepository.save(recuperacao);
        
        logger.info("Código validado com sucesso para usuário: {}", nomeUsuario);
        
        return new RecuperacaoSenhaResponseDTO(
            "Código validado com sucesso",
            tokenTemporario
        );
    }
    
    /**
     * Redefine senha usando token temporário
     */
    @Transactional
    public RecuperacaoSenhaResponseDTO redefinirSenha(String token, String novaSenha) {
        logger.info("Redefinição de senha com token");
        
        // Buscar recuperação por token
        RecuperacaoSenha recuperacao = recuperacaoRepository.findByTokenTemporarioValido(
            token,
            LocalDateTime.now()
        ).orElseThrow(() -> {
            logger.warn("Token inválido ou expirado");
            return new BusinessException("Token inválido ou expirado");
        });
        
        // Verificar se pode redefinir senha
        if (!recuperacao.podeRedefinirSenha()) {
            logger.warn("Token não pode ser usado para redefinir senha");
            
            if (recuperacao.getUsado()) {
                throw new BusinessException("Token já foi utilizado");
            }
            if (recuperacao.isExpirado()) {
                throw new BusinessException("Token expirado");
            }
            if (!recuperacao.getValidado()) {
                throw new BusinessException("Token não validado");
            }
            
            throw new BusinessException("Token inválido");
        }
        
        // Validar token JWT
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                .withIssuer("recuperacao-senha")
                .build()
                .verify(token);
        } catch (Exception e) {
            logger.error("Erro ao validar token JWT", e);
            throw new BusinessException("Token inválido");
        }
        
        // Atualizar senha do usuário
        Usuario usuario = recuperacao.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        
        // Marcar recuperação como usada
        recuperacao.setUsado(true);
        recuperacaoRepository.save(recuperacao);
        
        logger.info("Senha redefinida com sucesso para usuário: {}", usuario.getNomeUsuario());
        
        // Enviar email de confirmação
        try {
            emailService.enviarEmailSenhaAlterada(usuario.getEmail(), usuario.getNomeUsuario());
        } catch (Exception e) {
            logger.error("Erro ao enviar email de confirmação", e);
            // Não falhar a operação se o email não for enviado
        }
        
        return new RecuperacaoSenhaResponseDTO("Senha redefinida com sucesso");
    }
    
    /**
     * Invalida todos os códigos anteriores não usados do usuário
     */
    private void invalidarCodigosAnteriores(Long usuarioId) {
        var codigosAntigos = recuperacaoRepository.findCodigosNaoUsadosPorUsuario(usuarioId);
        codigosAntigos.forEach(c -> c.setUsado(true));
        recuperacaoRepository.saveAll(codigosAntigos);
    }
    
    /**
     * Gera código aleatório de 6 dígitos
     */
    private String gerarCodigoAleatorio() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // Gera número entre 100000 e 999999
        return String.valueOf(codigo);
    }
    
    /**
     * Gera token JWT temporário
     */
    private String gerarTokenTemporario(Usuario usuario, Long recuperacaoId) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
            .withIssuer("recuperacao-senha")
            .withSubject(usuario.getNomeUsuario())
            .withClaim("userId", usuario.getId().toString())
            .withClaim("recuperacaoId", recuperacaoId)
            .withExpiresAt(LocalDateTime.now().plusMinutes(EXPIRACAO_MINUTOS)
                .atZone(java.time.ZoneId.systemDefault()).toInstant())
            .sign(algorithm);
    }
    
    /**
     * Mascara email para exibição
     */
    private String mascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***@***.***";
        }
        
        String[] partes = email.split("@");
        String usuario = partes[0];
        String dominio = partes[1];
        
        String usuarioMascarado = usuario.length() > 2 
            ? usuario.charAt(0) + "***" 
            : "***";
        
        return usuarioMascarado + "@" + dominio;
    }
    
    /**
     * Obtém IP da requisição
     */
    private String obterIpRequisicao(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

