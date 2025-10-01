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
docker pull <seu-usuario>/lite-erp:latest
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://seu-host:5432/LiteERP \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=sua-senha \
  <seu-usuario>/lite-erp:latest
```

## 🔧 Variáveis de Ambiente (Docker)

| Variável | Descrição | Padrão |
|----------|-----------|---------|
| `SPRING_PROFILES_ACTIVE` | Profile ativo | `docker` |
| `DATABASE_URL` | URL do banco PostgreSQL | `jdbc:postgresql://liteerp-db:5432/LiteERP` |
| `DATABASE_USERNAME` | Usuário do banco | `postgres` |
| `DATABASE_PASSWORD` | Senha do banco | `postgres` |
| `JWT_SECRET` | Chave secreta JWT | `seguranca-top-production` |
| `SERVER_PORT` | Porta da aplicação | `8080` |

## 📚 Documentação da API

A documentação da API está disponível via Swagger UI:
- **Local**: `http://localhost:8080/swagger-ui.html`
- **Produção**: `http://seu-dominio/swagger-ui.html`

## 🔍 Health Check

O endpoint de health check está disponível em:
- `http://localhost:8080/actuator/health`

## 🚀 CI/CD

O projeto utiliza GitHub Actions para:
- ✅ Executar testes automatizados
- 🐳 Build e push da imagem Docker
- 🔒 Scan de segurança com Trivy
- 📝 Atualização automática da descrição no Docker Hub

### Configuração dos Secrets

Para que a pipeline funcione, configure os seguintes secrets no GitHub:
- `DOCKERHUB_USERNAME`: Seu usuário do Docker Hub
- `DOCKERHUB_TOKEN`: Token de acesso do Docker Hub

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Contato

Seu Nome - [@seu_twitter](https://twitter.com/seu_twitter) - seu.email@exemplo.com

Link do Projeto: [https://github.com/seu-usuario/LiteERP_Spring](https://github.com/seu-usuario/LiteERP_Spring)
