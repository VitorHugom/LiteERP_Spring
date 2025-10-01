# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder

# Instalar Maven
RUN apk add --no-cache maven

# Definir diretório de trabalho
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Baixar dependências (cache layer)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src src

# Construir a aplicação
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Definir diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo JAR do stage anterior
COPY --from=builder /app/target/lite-erp-0.0.1-SNAPSHOT.jar app.jar

# Configurar profile do Spring Boot
ENV SPRING_PROFILES_ACTIVE=docker

# Configurar variáveis de ambiente padrão (podem ser sobrescritas)
ENV DATABASE_URL=jdbc:postgresql://liteerp-db:5432/LiteERP
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=postgres
ENV JWT_SECRET=seguranca-top-production
ENV SERVER_PORT=8080

# Expor a porta em que o backend rodará
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
