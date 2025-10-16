package com.example.lite_erp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de Email usando SMTP2GO API
 */
@Configuration
public class EmailConfig {

    @Value("${smtp2go.api.key}")
    private String apiKey;

    @Value("${smtp2go.api.url:https://api.smtp2go.com/v3/email/send}")
    private String apiUrl;

    @Value("${smtp2go.from.email:noreply@lite-erp-enterprise.com}")
    private String fromEmail;

    @Value("${smtp2go.from.name:LiteERP}")
    private String fromName;

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getFromName() {
        return fromName;
    }
}

