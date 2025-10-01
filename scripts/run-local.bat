@echo off
echo ========================================
echo  LiteERP - Executar Ambiente Local
echo ========================================

echo.
echo 1. Iniciando banco de dados PostgreSQL...
docker-compose -f docker-compose.dev.yml up -d liteerp-db-dev

echo.
echo 2. Aguardando banco de dados ficar pronto...
timeout /t 10 /nobreak > nul

echo.
echo 3. Executando aplicacao com profile local...
echo.
echo IMPORTANTE: Configure o IntelliJ para usar o profile 'local'
echo Ou execute: mvn spring-boot:run -Plocal
echo.

pause
