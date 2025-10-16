package com.example.lite_erp.entities.recuperacao_senha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Long> {
    
    /**
     * Busca código de recuperação válido por usuário e código
     */
    @Query("""
        SELECT r FROM RecuperacaoSenha r
        WHERE r.usuario.id = :usuarioId
        AND r.codigo = :codigo
        AND r.validado = false
        AND r.usado = false
        AND r.dataExpiracao > :agora
        ORDER BY r.dataCriacao DESC
        """)
    Optional<RecuperacaoSenha> findCodigoValido(
        @Param("usuarioId") Long usuarioId,
        @Param("codigo") String codigo,
        @Param("agora") LocalDateTime agora
    );
    
    /**
     * Busca por token temporário válido
     */
    @Query("""
        SELECT r FROM RecuperacaoSenha r
        WHERE r.tokenTemporario = :token
        AND r.validado = true
        AND r.usado = false
        AND r.dataExpiracao > :agora
        """)
    Optional<RecuperacaoSenha> findByTokenTemporarioValido(
        @Param("token") String token,
        @Param("agora") LocalDateTime agora
    );
    
    /**
     * Invalida todos os códigos anteriores do usuário
     */
    @Query("""
        SELECT r FROM RecuperacaoSenha r
        WHERE r.usuario.id = :usuarioId
        AND r.usado = false
        """)
    List<RecuperacaoSenha> findCodigosNaoUsadosPorUsuario(@Param("usuarioId") Long usuarioId);
    
    /**
     * Remove códigos expirados (limpeza periódica)
     */
    void deleteByDataExpiracaoBefore(LocalDateTime data);
}

