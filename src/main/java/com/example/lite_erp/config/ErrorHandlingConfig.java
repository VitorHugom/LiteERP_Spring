package com.example.lite_erp.config;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Configuração para personalizar atributos de erro
 */
@Configuration
public class ErrorHandlingConfig {
    
    /**
     * Customiza os atributos de erro padrão do Spring Boot
     */
    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, 
                                                         org.springframework.boot.web.error.ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                
                // Remover informações sensíveis
                errorAttributes.remove("exception");
                errorAttributes.remove("trace");
                
                // Adicionar código de erro customizado se não existir
                if (!errorAttributes.containsKey("errorCode")) {
                    errorAttributes.put("errorCode", "GENERIC_ERROR");
                }
                
                return errorAttributes;
            }
        };
    }
}
