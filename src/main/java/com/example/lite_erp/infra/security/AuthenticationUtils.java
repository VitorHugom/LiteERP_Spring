package com.example.lite_erp.infra.security;

import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.infra.exceptions.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utilitário para facilitar o acesso ao usuário autenticado no contexto de segurança
 */
@Component
public class AuthenticationUtils {

    /**
     * Obtém o usuário logado do contexto de segurança
     * 
     * @return Usuario logado
     * @throws AuthenticationException se não houver usuário autenticado
     */
    public static Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Usuário não autenticado", "USER_NOT_AUTHENTICATED");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (!(principal instanceof Usuario)) {
            throw new AuthenticationException("Principal não é uma instância de Usuario", "INVALID_PRINCIPAL_TYPE");
        }
        
        return (Usuario) principal;
    }

    /**
     * Obtém o ID do usuário logado
     * 
     * @return ID do usuário logado
     * @throws AuthenticationException se não houver usuário autenticado
     */
    public static Long getUsuarioLogadoId() {
        return getUsuarioLogado().getId();
    }

    /**
     * Obtém o nome do usuário logado
     * 
     * @return Nome do usuário logado
     * @throws AuthenticationException se não houver usuário autenticado
     */
    public static String getUsuarioLogadoNome() {
        return getUsuarioLogado().getNomeUsuario();
    }

    /**
     * Verifica se há um usuário autenticado
     * 
     * @return true se há usuário autenticado, false caso contrário
     */
    public static boolean isUsuarioAutenticado() {
        try {
            getUsuarioLogado();
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }
}
