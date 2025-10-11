package com.example.lite_erp.infra.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * DESABILITADO: A configuração de CORS está sendo feita no SecurityConfig.java
 * Esta classe foi desabilitada para evitar conflitos de configuração.
 */
//@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://lite-erp-enterprise.com",
                        "https://*.lite-erp-enterprise.com",
                        "http://localhost:4200",
                        "http://192.168.78.172:4200"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization");
    }
}
