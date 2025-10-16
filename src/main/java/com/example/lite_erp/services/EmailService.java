package com.example.lite_erp.services;

import com.example.lite_erp.config.EmailConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service para envio de emails usando SMTP2GO API
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailConfig emailConfig;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Envia email de forma assíncrona usando SMTP2GO API
     */
    @Async
    public void enviarEmail(String destinatario, String assunto, String corpoHtml) {
        try {
            Map<String, Object> emailData = new HashMap<>();
            emailData.put("api_key", emailConfig.getApiKey());
            emailData.put("to", List.of(destinatario));
            emailData.put("sender", emailConfig.getFromEmail());
            emailData.put("subject", assunto);
            emailData.put("html_body", corpoHtml);

            String jsonBody = objectMapper.writeValueAsString(emailData);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(emailConfig.getApiUrl()))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                logger.info("Email enviado com sucesso para: {}", destinatario);
            } else {
                logger.error("Erro ao enviar email. Status: {}, Response: {}", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            logger.error("Erro ao enviar email para {}: {}", destinatario, e.getMessage(), e);
        }
    }

    /**
     * Envia email com código de recuperação de senha
     */
    @Async
    public void enviarEmailCodigoRecuperacao(String destinatario, String nomeUsuario, String codigo, int expiracaoMinutos) {
        String assunto = "Código de Recuperação de Senha - LiteERP";
        
        String corpoHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                    }
                    .container {
                        background-color: #f9f9f9;
                        border-radius: 10px;
                        padding: 30px;
                        border: 1px solid #ddd;
                    }
                    .header {
                        text-align: center;
                        color: #2c3e50;
                        margin-bottom: 30px;
                    }
                    .code-box {
                        background-color: #3498db;
                        color: white;
                        font-size: 32px;
                        font-weight: bold;
                        text-align: center;
                        padding: 20px;
                        border-radius: 8px;
                        letter-spacing: 8px;
                        margin: 30px 0;
                    }
                    .info {
                        background-color: #fff3cd;
                        border-left: 4px solid #ffc107;
                        padding: 15px;
                        margin: 20px 0;
                    }
                    .footer {
                        text-align: center;
                        color: #7f8c8d;
                        font-size: 12px;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 1px solid #ddd;
                    }
                    .warning {
                        background-color: #f8d7da;
                        border-left: 4px solid #dc3545;
                        padding: 15px;
                        margin: 20px 0;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔐 Recuperação de Senha</h1>
                        <p>LiteERP</p>
                    </div>
                    
                    <p>Olá, <strong>%s</strong>!</p>
                    
                    <p>Você solicitou a recuperação de senha da sua conta no LiteERP.</p>
                    
                    <p>Use o código abaixo para validar sua identidade:</p>
                    
                    <div class="code-box">
                        %s
                    </div>
                    
                    <div class="info">
                        <strong>⏰ Atenção:</strong> Este código expira em <strong>%d minutos</strong>.
                    </div>
                    
                    <p><strong>Instruções:</strong></p>
                    <ol>
                        <li>Copie o código acima</li>
                        <li>Cole na tela de recuperação de senha</li>
                        <li>Defina sua nova senha</li>
                    </ol>
                    
                    <div class="warning">
                        <strong>⚠️ Importante:</strong> Se você não solicitou esta recuperação, ignore este email. 
                        Sua senha permanecerá inalterada.
                    </div>
                    
                    <div class="footer">
                        <p>Este é um email automático, por favor não responda.</p>
                        <p>&copy; %d LiteERP - Sistema de Gestão Empresarial</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nomeUsuario, codigo, expiracaoMinutos, LocalDateTime.now().getYear());

        enviarEmail(destinatario, assunto, corpoHtml);
    }

    /**
     * Envia email de confirmação de alteração de senha
     */
    @Async
    public void enviarEmailSenhaAlterada(String destinatario, String nomeUsuario) {
        String assunto = "Senha Alterada com Sucesso - LiteERP";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        String dataHora = LocalDateTime.now().format(formatter);
        
        String corpoHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                    }
                    .container {
                        background-color: #f9f9f9;
                        border-radius: 10px;
                        padding: 30px;
                        border: 1px solid #ddd;
                    }
                    .header {
                        text-align: center;
                        color: #27ae60;
                        margin-bottom: 30px;
                    }
                    .success-box {
                        background-color: #d4edda;
                        border-left: 4px solid #28a745;
                        padding: 20px;
                        margin: 20px 0;
                        border-radius: 5px;
                    }
                    .info-box {
                        background-color: #e7f3ff;
                        border-left: 4px solid #2196F3;
                        padding: 15px;
                        margin: 20px 0;
                    }
                    .warning {
                        background-color: #fff3cd;
                        border-left: 4px solid #ffc107;
                        padding: 15px;
                        margin: 20px 0;
                    }
                    .footer {
                        text-align: center;
                        color: #7f8c8d;
                        font-size: 12px;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 1px solid #ddd;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>✅ Senha Alterada com Sucesso</h1>
                        <p>LiteERP</p>
                    </div>
                    
                    <p>Olá, <strong>%s</strong>!</p>
                    
                    <div class="success-box">
                        <strong>✓ Confirmação:</strong> Sua senha foi alterada com sucesso!
                    </div>
                    
                    <div class="info-box">
                        <strong>📅 Data e Hora:</strong> %s
                    </div>
                    
                    <p>Você já pode fazer login no LiteERP usando sua nova senha.</p>
                    
                    <div class="warning">
                        <strong>⚠️ Você não fez esta alteração?</strong>
                        <p>Se você não solicitou a alteração de senha, entre em contato imediatamente com o suporte:</p>
                        <ul>
                            <li>Acesse sua conta e altere a senha novamente</li>
                            <li>Entre em contato com o administrador do sistema</li>
                        </ul>
                    </div>
                    
                    <p><strong>Dicas de Segurança:</strong></p>
                    <ul>
                        <li>Use senhas fortes e únicas</li>
                        <li>Não compartilhe sua senha com ninguém</li>
                        <li>Altere sua senha periodicamente</li>
                        <li>Não use a mesma senha em diferentes sistemas</li>
                    </ul>
                    
                    <div class="footer">
                        <p>Este é um email automático, por favor não responda.</p>
                        <p>&copy; %d LiteERP - Sistema de Gestão Empresarial</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nomeUsuario, dataHora, LocalDateTime.now().getYear());

        enviarEmail(destinatario, assunto, corpoHtml);
    }
}

