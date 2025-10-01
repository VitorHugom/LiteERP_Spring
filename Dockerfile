# Dockerfile para aplicação Spring Boot
# Usando multi-stage build para otimizar o tamanho da imagem

# Stage 1: Build da aplicação
FROM maven:3.9.4-openjdk-17-slim AS build

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

# Fazer o build da aplicação com profile docker
RUN mvn clean package -DskipTests -Pdocker

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

# Instalar curl para health checks
RUN apk add --no-cache curl

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR da aplicação do stage de build
COPY --from=build /app/target/*.jar app.jar

# Mudar ownership para o usuário spring
RUN chown spring:spring app.jar

# Mudar para usuário não-root
USER spring:spring

# Expor porta da aplicação
EXPOSE 8080

# Configurar JVM para containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Configurar profile do Spring Boot
ENV SPRING_PROFILES_ACTIVE=docker

# Configurar variáveis de ambiente padrão (podem ser sobrescritas)
ENV DATABASE_URL=jdbc:postgresql://liteerp-db:5432/LiteERP
ENV DATABASE_USERNAME=postgres
ENV DATABASE_PASSWORD=postgres
ENV JWT_SECRET=seguranca-top-production
ENV SERVER_PORT=8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar app.jar"]
