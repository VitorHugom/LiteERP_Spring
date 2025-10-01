# 🚀 Tutorial: Cloudflare Tunnel para LiteERP (Configuração com Arquivos de Credenciais)

Este tutorial ensina como configurar o Cloudflare Tunnel usando arquivos de credenciais para expor sua aplicação LiteERP na internet de forma segura, sem precisar abrir portas no firewall.

## 📋 **Pré-requisitos**

- ✅ Conta no Cloudflare (gratuita)
- ✅ Domínio configurado no Cloudflare
- ✅ Docker e Docker Compose instalados
- ✅ Cloudflared CLI (apenas para configuração inicial)
- ✅ Aplicação LiteERP funcionando localmente

## 🎯 **Visão Geral da Configuração**

Este tutorial usa **arquivos de credenciais** em vez de tokens, oferecendo:
- ✅ **Maior segurança** - Credenciais ficam em arquivos locais
- ✅ **Facilidade de deploy** - Basta copiar 2 arquivos para novos servidores
- ✅ **Múltiplas empresas** - Cada empresa pode ter seu próprio tunnel
- ✅ **Zero configuração** - Tudo funciona após copiar os arquivos

## 🔧 **Passo 1: Instalar Cloudflared CLI (Apenas para Configuração Inicial)**

⚠️ **Nota:** O CLI é necessário apenas para criar o tunnel e gerar as credenciais. Após isso, tudo funciona via Docker.

### **Windows:**
```bash
# Baixar da página oficial
# https://github.com/cloudflare/cloudflared/releases
# Baixe: cloudflared-windows-amd64.exe
```

### **Linux:**
```bash
# Ubuntu/Debian
wget https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64.deb
sudo dpkg -i cloudflared-linux-amd64.deb

# CentOS/RHEL/Fedora  
wget https://github.com/cloudflare/cloudflared/releases/latest/download/cloudflared-linux-amd64.rpm
sudo rpm -i cloudflared-linux-amd64.rpm
```

### **macOS:**
```bash
brew install cloudflared
```

## 🔐 **Passo 2: Autenticar com Cloudflare**

```bash
# Fazer login (abre o navegador)
cloudflared tunnel login
```

Isso criará o arquivo `cert.pem` em:
- **Windows:** `C:\Users\{usuario}\.cloudflared\cert.pem`
- **Linux/macOS:** `~/.cloudflared/cert.pem`

## 🚇 **Passo 3: Criar o Tunnel**

```bash
# Criar tunnel (substitua 'empresa-nome' pelo nome da sua empresa)
cloudflared tunnel create empresa-nome

# Exemplo:
cloudflared tunnel create liteerp-empresa1
```

Isso criará o arquivo de credenciais:
- **Windows:** `C:\Users\{usuario}\.cloudflared\{tunnel-id}.json`
- **Linux/macOS:** `~/.cloudflared/{tunnel-id}.json`

**Anote o TUNNEL_ID que aparecerá na tela!**

## 📁 **Passo 4: Copiar Arquivos de Credenciais**

Copie os 2 arquivos gerados para a pasta `cloudflared/` do seu projeto:

```bash
# Criar pasta no projeto
mkdir cloudflared

# Copiar arquivos (Windows)
copy "C:\Users\{usuario}\.cloudflared\cert.pem" "cloudflared\"
copy "C:\Users\{usuario}\.cloudflared\{tunnel-id}.json" "cloudflared\"

# Copiar arquivos (Linux/macOS)
cp ~/.cloudflared/cert.pem cloudflared/
cp ~/.cloudflared/{tunnel-id}.json cloudflared/
```

## ⚙️ **Passo 5: Configurar config.yml**

Edite o arquivo `config.yml` na raiz do projeto:

```yaml
tunnel: SEU_TUNNEL_ID_AQUI
credentials-file: /etc/cloudflared/SEU_TUNNEL_ID_AQUI.json

ingress:
  - hostname: seudominio.com
    service: http://liteerp-frontend:80
  - hostname: api.seudominio.com
    service: http://liteerp-backend:8080
  - service: http_status:404
```

**Substitua:**
- `SEU_TUNNEL_ID_AQUI` pelo ID do tunnel criado
- `seudominio.com` pelo seu domínio real

## 🌐 **Passo 6: Configurar DNS no Cloudflare**

1. **Acesse:** https://dash.cloudflare.com/
2. **Selecione seu domínio**
3. **Vá em DNS**
4. **Adicione registros CNAME:**

| Tipo  | Nome | Destino |
|-------|------|---------|
| CNAME | @    | `{tunnel-id}.cfargotunnel.com` |
| CNAME | api  | `{tunnel-id}.cfargotunnel.com` |

**Substitua `{tunnel-id}` pelo ID do seu tunnel.**

## 🚀 **Passo 7: Executar o Deploy**

```bash
# Iniciar containers de produção
docker-compose -f docker-compose.prod.yml up -d
```

## ✅ **Passo 8: Verificar se Funcionou**

1. **Verificar containers:**
   ```bash
   docker-compose -f docker-compose.prod.yml ps
   ```

2. **Verificar logs do tunnel:**
   ```bash
   docker-compose -f docker-compose.prod.yml logs cloudflared
   ```

3. **Testar URLs:**
   - Frontend: https://seudominio.com
   - API: https://api.seudominio.com
   - Swagger: https://api.seudominio.com/swagger-ui/index.html

## 🏢 **Configuração para Múltiplas Empresas**

Para implantar o LiteERP em várias empresas, cada uma precisa de seu próprio tunnel:

### **Para cada nova empresa:**

1. **Criar novo tunnel:**
   ```bash
   cloudflared tunnel create empresa2-nome
   ```

2. **Copiar credenciais:**
   ```bash
   # Criar pasta específica
   mkdir cloudflared-empresa2
   
   # Copiar arquivos
   cp ~/.cloudflared/cert.pem cloudflared-empresa2/
   cp ~/.cloudflared/{novo-tunnel-id}.json cloudflared-empresa2/
   ```

3. **Criar config específico:**
   ```yaml
   # config-empresa2.yml
   tunnel: NOVO_TUNNEL_ID
   credentials-file: /etc/cloudflared/NOVO_TUNNEL_ID.json
   
   ingress:
     - hostname: empresa2.com
       service: http://liteerp-frontend:80
     - hostname: api.empresa2.com
       service: http://liteerp-backend:8080
     - service: http_status:404
   ```

4. **Ajustar docker-compose:**
   ```yaml
   # docker-compose-empresa2.yml
   cloudflared:
     image: cloudflare/cloudflared:latest
     container_name: liteerp-cloudflared-empresa2
     command: tunnel --config /etc/cloudflared/config-empresa2.yml run
     volumes:
       - ./config-empresa2.yml:/etc/cloudflared/config-empresa2.yml:ro
       - ./cloudflared-empresa2:/etc/cloudflared
   ```

5. **Configurar DNS da empresa2:**
   - Adicionar CNAMEs apontando para `{novo-tunnel-id}.cfargotunnel.com`

### **Estrutura para Múltiplas Empresas:**

```
📁 Projeto/
├── config.yml                    # Empresa 1
├── config-empresa2.yml           # Empresa 2
├── config-empresa3.yml           # Empresa 3
├── docker-compose.prod.yml       # Empresa 1
├── docker-compose-empresa2.yml   # Empresa 2
├── docker-compose-empresa3.yml   # Empresa 3
├── cloudflared/                  # Credenciais Empresa 1
│   ├── cert.pem
│   └── tunnel1.json
├── cloudflared-empresa2/         # Credenciais Empresa 2
│   ├── cert.pem
│   └── tunnel2.json
└── cloudflared-empresa3/         # Credenciais Empresa 3
    ├── cert.pem
    └── tunnel3.json
```

## 📋 **Arquivos Necessários para Novos Deploys**

Para configurar o tunnel em um novo servidor, você precisa destes arquivos:

```
📁 Projeto/
├── config.yml             # Configuração do tunnel (público)
├── docker-compose.prod.yml # Configuração Docker (todas as variáveis definidas)
└── cloudflared/           # Pasta com credenciais (PRIVADA)
    ├── cert.pem           # Certificado do Cloudflare
    └── [tunnel-id].json   # Credenciais do tunnel específico
```

**⚠️ IMPORTANTE:** 
- A pasta `cloudflared/` contém dados sensíveis e **NÃO deve ser commitada**
- Copie estes arquivos manualmente para novos servidores
- O `config.yml` pode ser commitado pois contém apenas configuração pública

## 🔒 **Segurança**

- ✅ **Nunca commite** a pasta `cloudflared/` com credenciais
- ✅ **Use** arquivos de configuração separados para cada empresa
- ✅ **Proteja** os arquivos cert.pem e tunnel.json
- ✅ **Rotacione** credenciais periodicamente
- ✅ **Use** tunnels separados para cada cliente/empresa

## 🎯 **Resultado Final**

Após seguir este tutorial, você terá:
- ✅ Aplicação acessível via HTTPS
- ✅ SSL automático via Cloudflare
- ✅ Proteção DDoS
- ✅ Cache global
- ✅ Analytics do Cloudflare
- ✅ Configuração escalável para múltiplas empresas

**URLs de acesso:**
- **Frontend:** https://seudominio.com
- **API:** https://api.seudominio.com
- **Documentação:** https://api.seudominio.com/swagger-ui/index.html

## 🔍 **Troubleshooting**

### **Problema: Tunnel não conecta**
```bash
# Verificar logs
docker-compose -f docker-compose.prod.yml logs cloudflared

# Verificar se arquivos existem
ls -la cloudflared/
```

### **Problema: DNS não resolve**
- Aguarde propagação DNS (até 24h)
- Verifique se os CNAMEs estão corretos
- Use `nslookup seudominio.com`

### **Problema: Aplicação não responde**
```bash
# Verificar se backend está rodando
docker-compose -f docker-compose.prod.yml logs liteerp-backend

# Verificar saúde dos containers
docker-compose -f docker-compose.prod.yml ps
```

## 📚 **Comandos Úteis**

```bash
# Listar tunnels
cloudflared tunnel list

# Informações do tunnel
cloudflared tunnel info SEU_TUNNEL_ID

# Deletar tunnel
cloudflared tunnel delete SEU_TUNNEL_ID

# Ver logs em tempo real
docker-compose -f docker-compose.prod.yml logs -f cloudflared

# Parar containers
docker-compose -f docker-compose.prod.yml down

# Iniciar containers
docker-compose -f docker-compose.prod.yml up -d
```
