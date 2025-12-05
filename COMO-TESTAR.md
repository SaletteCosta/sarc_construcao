# 🧪 Guia de Testes - SARC

## Pré-requisitos

- Docker Desktop instalado e rodando
- Portas livres: 3000, 5433, 8080, 8084, 8085, 8761, 9090, 3000

## 1️⃣ Iniciar o Sistema

### Opção A: Com Frontend (branch frontend-Eduardo)
```bash
git checkout frontend-Eduardo
docker compose up --build
```

### Opção B: Apenas Backend (branch Eduardo-T3)
```bash
git checkout Eduardo-T3
docker compose up --build
```

**Aguarde 2-3 minutos** para todos os serviços subirem.

---

## 2️⃣ Verificar se os Serviços Subiram

### Ver status dos containers:
```bash
docker compose ps
```

**Esperado:** Todos os serviços como "Up" ou "healthy"

### Ver logs em tempo real:
```bash
docker compose logs -f
```

---

## 3️⃣ Testar Service Discovery (Eureka)

### Abrir Eureka Dashboard:
```bash
open http://localhost:8761
```
Ou acesse manualmente: http://localhost:8761

**O que verificar:**
- ✅ **ADMIN-SERVICE** deve aparecer registrado
- ✅ **USER-SERVICE** deve aparecer registrado  
- ✅ **API-GATEWAY** deve aparecer registrado

Se os 3 serviços estiverem lá, o Eureka está funcionando! 🎉

---

## 4️⃣ Testar API Gateway

### 4.1 Health Check do Gateway
```bash
curl http://localhost:8080/actuator/health
```

**Esperado:**
```json
{"status":"UP"}
```

### 4.2 Testar Roteamento para Admin Service

**Via Gateway:**
```bash
curl http://localhost:8080/api/admin/classes
```

**Direto no serviço (para comparar):**
```bash
curl http://localhost:8084/classes
```

**O que verificar:**
- ✅ Ambos devem retornar a mesma resposta
- ✅ Se retornar lista vazia `[]` está OK (banco está vazio)
- ✅ Se retornar erro 404, algo está errado

### 4.3 Testar Roteamento para User Service

**Via Gateway:**
```bash
curl http://localhost:8080/api/user/users
```

**Direto no serviço:**
```bash
curl http://localhost:8085/users
```

---

## 5️⃣ Testar Observabilidade

### 5.1 Verificar Métricas Actuator

**Admin Service:**
```bash
curl http://localhost:8084/actuator/prometheus | head -20
```

**User Service:**
```bash
curl http://localhost:8085/actuator/prometheus | head -20
```

**Gateway:**
```bash
curl http://localhost:8080/actuator/prometheus | head -20
```

**O que verificar:**
- ✅ Deve retornar muitas linhas de métricas
- ✅ Procure por: `jvm_memory_used_bytes`, `http_server_requests_seconds`

### 5.2 Verificar Prometheus

**Abrir Prometheus:**
```bash
open http://localhost:9090
```

**Verificar Targets:**
1. Acesse: http://localhost:9090/targets
2. Verifique que todos estão **UP**:
   - prometheus
   - api-gateway
   - admin-service
   - user-service
   - eureka-server

**Testar Query:**
1. Vá para: http://localhost:9090/graph
2. Digite a query: `up`
3. Clique em "Execute"
4. **Esperado:** Deve mostrar valor `1` para todos os serviços

### 5.3 Verificar Grafana

**Abrir Grafana:**
```bash
open http://localhost:3000
```
- **Login:** admin
- **Senha:** admin

**Verificar Datasource:**
1. Menu (☰) → Configuration → Data Sources
2. Clique em "Prometheus"
3. Role até o final e clique em "Save & Test"
4. **Esperado:** "Data source is working" ✅

**Verificar Dashboard:**
1. Menu → Dashboards → Browse
2. Procure por "SARC Microservices Overview"
3. Clique para abrir
4. **O que verificar:**
   - Painéis carregando
   - Gráficos com dados (pode demorar alguns segundos)

---

## 6️⃣ Testar com Dados Reais

### 6.1 Criar uma Classe (Admin Service)

```bash
curl -X POST http://localhost:8080/api/admin/classes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Construção de Software",
    "code": "CS-2024",
    "semester": "2024/2"
  }'
```

**Esperado:** Retorno com a classe criada e ID

### 6.2 Listar Classes

```bash
curl http://localhost:8080/api/admin/classes
```

**Esperado:** Lista com a classe que acabou de criar

### 6.3 Criar um Usuário (User Service)

```bash
curl -X POST http://localhost:8080/api/user/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "registration": "2024001",
    "type": "STUDENT"
  }'
```

### 6.4 Listar Usuários

```bash
curl http://localhost:8080/api/user/users
```

---

## 7️⃣ Testar Frontend (se na branch frontend-Eduardo)

### Abrir no navegador:
```bash
open http://localhost:3000
```

**O que testar:**
1. ✅ Dashboard carrega
2. ✅ Sidebar funciona
3. ✅ Navegue para "Classes" → deve listar as classes
4. ✅ Navegue para "Users" → deve listar os usuários
5. ✅ Tente criar uma nova classe
6. ✅ Tente criar um novo usuário

---

## 8️⃣ Gerar Carga para Ver Métricas

### Script para gerar requisições:
```bash
for i in {1..100}; do
  curl -s http://localhost:8080/api/admin/classes > /dev/null
  echo "Request $i completed"
  sleep 0.1
done
```

**Depois:**
1. Vá para Grafana (http://localhost:3000)
2. Abra o dashboard "SARC Microservices Overview"
3. **Deve ver:** Aumento nas requisições HTTP e uso de memória

---

## 9️⃣ Testar Resiliência

### Teste: Parar um serviço e ver Eureka detectar

```bash
# Parar o admin-service
docker compose stop admin-service

# Aguardar ~30 segundos

# Verificar no Eureka
open http://localhost:8761
```

**O que verificar:**
- ✅ admin-service deve sumir da lista do Eureka
- ✅ Gateway deve retornar erro 503 ao tentar acessar /api/admin/**

**Restaurar:**
```bash
docker compose start admin-service
```

Aguarde ~30s e o serviço volta a aparecer no Eureka!

---

## 🔟 Testes de Swagger (Documentação API)

### Admin Service:
```bash
open http://localhost:8084/swagger-ui.html
```

### User Service:
```bash
open http://localhost:8085/swagger-ui.html
```

**O que fazer:**
1. Explore os endpoints disponíveis
2. Clique em "Try it out" para testar
3. Execute requests direto do Swagger

---

## ✅ Checklist de Validação Completa

Execute cada item e marque se passou:

### Infraestrutura
- [ ] Todos os 7 containers subiram sem erro
- [ ] `docker compose ps` mostra todos como "Up"
- [ ] Não há portas em conflito

### Service Discovery (Eureka)
- [ ] Eureka Dashboard acessível (http://localhost:8761)
- [ ] admin-service registrado
- [ ] user-service registrado
- [ ] api-gateway registrado

### API Gateway
- [ ] Gateway health check OK
- [ ] Rota /api/admin/** funciona
- [ ] Rota /api/user/** funciona
- [ ] Métricas do gateway expostas

### Observabilidade
- [ ] Prometheus acessível (http://localhost:9090)
- [ ] Todos os targets UP no Prometheus
- [ ] Grafana acessível (http://localhost:3000)
- [ ] Dashboard carrega com dados
- [ ] Métricas sendo coletadas

### Funcionalidade
- [ ] Consegue criar uma classe via API
- [ ] Consegue criar um usuário via API
- [ ] Consegue listar dados criados
- [ ] Swagger das APIs funciona

### Frontend (se aplicável)
- [ ] Frontend acessível (http://localhost:3000)
- [ ] Consegue navegar entre páginas
- [ ] Consegue criar dados via interface
- [ ] Dados aparecem nas listagens

---

## 🐛 Troubleshooting

### Problema: Containers não sobem
```bash
# Ver logs com erros
docker compose logs

# Limpar e tentar novamente
docker compose down -v
docker compose up --build
```

### Problema: Serviços não registram no Eureka
```bash
# Ver logs do Eureka
docker compose logs eureka-server

# Ver logs do serviço específico
docker compose logs admin-service

# Reiniciar serviços
docker compose restart admin-service user-service
```

### Problema: Prometheus não coleta métricas
```bash
# Verificar targets
open http://localhost:9090/targets

# Testar endpoint manualmente
curl http://localhost:8084/actuator/prometheus

# Ver logs do Prometheus
docker compose logs prometheus
```

### Problema: Gateway não roteia
```bash
# Ver logs do gateway
docker compose logs api-gateway

# Verificar se está registrado no Eureka
curl http://localhost:8761/eureka/apps

# Verificar health
curl http://localhost:8080/actuator/health
```

---

## 📊 Queries Úteis do Prometheus

Copie e cole no Prometheus (http://localhost:9090/graph):

```promql
# Ver quais serviços estão UP
up

# Taxa de requisições HTTP
rate(http_server_requests_seconds_count[1m])

# Uso de memória JVM
jvm_memory_used_bytes{area="heap"}

# Requisições por endpoint
sum by (uri) (http_server_requests_seconds_count)

# Tempo de resposta p95
histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))
```

---

## 🎯 Teste Rápido (5 minutos)

Se tiver pouco tempo, teste apenas isso:

```bash
# 1. Subir sistema
docker compose up -d --build

# 2. Aguardar 2 minutos
sleep 120

# 3. Testar Gateway
curl http://localhost:8080/api/admin/classes

# 4. Verificar Eureka
open http://localhost:8761

# 5. Ver métricas no Grafana
open http://localhost:3000
# Login: admin/admin
```

Se tudo isso funcionar, está OK! ✅

---

## 🚀 Comando Final: Parar Tudo

```bash
docker compose down
```

Para limpar volumes também:
```bash
docker compose down -v
```

---

**Pronto! Agora você sabe testar toda a implementação!** 🎉
