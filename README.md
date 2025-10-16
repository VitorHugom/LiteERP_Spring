# LiteERP - Sistema ERP Básico

Um sistema ERP básico desenvolvido em Spring Boot para pequenas empresas.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.3.3**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Migrations)
- **JWT** (Autenticação)
- **SpringDoc OpenAPI** (Documentação da API)
- **Docker**
- **Cloudflare Tunnel** (Deploy em produção)

## 📚 Documentação

### 📖 Guias de Configuração:
- **[TUTORIAL-CLOUDFLARE-TUNNEL.md](TUTORIAL-CLOUDFLARE-TUNNEL.md)** - Guia passo a passo completo para deploy
- **[CHECKLIST-DEPLOY.md](CHECKLIST-DEPLOY.md)** - Lista de verificação antes do deploy
- **[CONFIGURACAO-SMTP2GO.md](CONFIGURACAO-SMTP2GO.md)** - Configuração de email com SMTP2GO

## 🌐 Deploy com Cloudflare Tunnel

### ⚡ Deploy Rápido:
```bash
# 1. Copiar arquivos de credenciais para cloudflared/
# 2. Ajustar config.yml com seu domínio
# 3. Executar deploy
docker-compose -f docker-compose.prod.yml up -d
```

**Estrutura necessária:**
```
cloudflared/
├── cert.pem           # Certificado do Cloudflare
└── {tunnel-id}.json   # Credenciais do tunnel
```

## 📋 Pré-requisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Docker (opcional)

## 🔧 Configuração Local

### 1. Clone o repositório
```bash
git clone <url-do-repositorio>
cd LiteERP_Spring
```

### 2. Inicie o banco de dados local
```bash
# Usando o script (Windows)
scripts/run-local.bat

# Ou manualmente
docker-compose -f docker-compose.dev.yml up -d
```

### 3. Execute a aplicação

#### Via IntelliJ IDEA:
1. Configure o profile ativo: `local`
2. Execute a classe principal

#### Via Maven:
```bash
# Profile local (padrão)
./mvnw spring-boot:run -Plocal

# Profile docker
./mvnw spring-boot:run -Pdocker

# Executar testes
./mvnw test
```

A aplicação estará disponível em: `http://localhost:8080`

## 📁 Estrutura do Projeto

```
📁 LiteERP_Spring/
├── src/main/java/           # Código fonte Java
├── src/main/resources/      # Recursos e configurações
│   ├── db/migration/        # Scripts Flyway
│   └── application*.yml     # Configurações por profile
├── config.yml              # Configuração Cloudflare Tunnel
├── docker-compose*.yml     # Configurações Docker
├── cloudflared/            # Credenciais Cloudflare (não commitado)
│   ├── cert.pem
│   └── {tunnel-id}.json
└── scripts/                # Scripts utilitários
    └── run-local.bat
```

## 📋 Profiles Disponíveis

### 🏠 Local (`local`)
- **Uso**: Desenvolvimento no IntelliJ
- **Banco**: PostgreSQL local (localhost:54320)
- **Logs**: Detalhados para debug
- **Ativo por padrão**

### 🐳 Docker (`docker`)
- **Uso**: Containers e produção
- **Banco**: Via variáveis de ambiente
- **Logs**: Otimizados para produção
- **Configuração via ENV vars**



## 🐳 Docker

### Executar com Docker Compose (Recomendado)
```bash
# Ambiente completo (app + banco)
docker-compose up -d

# Apenas banco para desenvolvimento
docker-compose -f docker-compose.dev.yml up -d
```

### Executar com Docker
```bash
# Build da imagem
docker build -t lite-erp .

# Executar container com variáveis de ambiente
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://seu-host:5432/LiteERP \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=sua-senha \
  -e JWT_SECRET=seu-jwt-secret \
  lite-erp
```

### Docker Hub
A imagem está disponível no Docker Hub e é automaticamente atualizada via GitHub Actions:

```bash
docker pull vitorhugoms/lite-erp-backend:latest
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://seu-host:5432/LiteERP \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=sua-senha \
  vitorhugoms/lite-erp-backend:latest
```

## ⚡ Comandos Essenciais

### Desenvolvimento Local:
```bash
# Iniciar banco de dados
docker-compose -f docker-compose.dev.yml up -d

# Executar aplicação
./mvnw spring-boot:run -Plocal

# Executar testes
./mvnw test
```

### Produção:
```bash
# Deploy completo
docker-compose -f docker-compose.prod.yml up -d

# Ver logs
docker-compose -f docker-compose.prod.yml logs -f

# Parar aplicação
docker-compose -f docker-compose.prod.yml down
```

## 📚 Documentação da API

A documentação da API está disponível via Swagger UI:
- **Local**: `http://localhost:8080/swagger-ui/index.html`
- **Produção**: `https://api.seu-dominio.com/swagger-ui/index.html`

## 🔍 Health Check

O endpoint de health check está disponível em:
- `http://localhost:8080/actuator/health`

## 🚀 CI/CD

O projeto utiliza GitHub Actions para:
- ✅ Executar testes automatizados
- 🐳 Build e push da imagem Docker
- 🔒 Scan de segurança com Trivy

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Contato

Vitor Hugo - vitorhugoms@outlook.com

Link do Projeto: [https://github.com/vitorhugoms/LiteERP_Spring](https://github.com/vitorhugoms/LiteERP_Spring)
