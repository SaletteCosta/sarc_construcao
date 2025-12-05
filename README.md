# Closed CRAS

## Grupo

- **Eduardo Martignoni**
- **Nicoli de Oliveira Siqueira**
- **Ruan da Rosa Necker**
- **Salette da Costa**
- **Vicenzo Mattos Frusciante**
- **Vitória Suelen Sampaio Graff**

## Diagramas

### Diagrama Relacional 
<img width="1038" height="574" alt="CS_MODEL" src="https://github.com/user-attachments/assets/09e97f73-7979-4111-bb50-2b9553d27e6a" />

# Diagrama Lógico 
### Diagrama Lógico 
<img width="1192" height="834" alt="Untitled" src="https://github.com/user-attachments/assets/4273c508-631f-46fa-a57a-d33e33b5aba6" />

##  Como executar

```bash
docker compose up --build
```

##  Arquitetura de Microserviços

### Frontend
- **Frontend** (3000): Interface web React

### Serviços de Negócio
- **Admin Service** (8084→8081): Gerencia turmas, disciplinas e professores
- **User Service** (8085→8082): Gerencia usuários e reservas

### Infraestrutura
- **API Gateway** (8080): Ponto único de entrada para todos os serviços
- **Eureka Server** (8761): Service Discovery e Registry
- **PostgreSQL** (5433): Banco de dados compartilhado

### Observabilidade (OTEL)
- **Prometheus** (9090): Coleta e armazena métricas
- **Grafana** (3000): Dashboards e visualização de métricas
  - Login: admin/admin

##  Testes

```bash
cd class && mvn test
 
cd user && mvn test
```

##  Documentação

### Frontend
- **Frontend**: http://localhost:3000

### APIs
- **API Gateway**: http://localhost:8080
  - Admin endpoints: `/api/admin/**`
  - User endpoints: `/api/user/**`
- **Swagger Admin**: http://localhost:8084/swagger-ui.html
- **Swagger User**: http://localhost:8085/swagger-ui.html

### Monitoramento
- **Eureka Dashboard**: http://localhost:8761
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)

##  Tecnologias

### Backend
- Java 17
- Spring Boot 3.3.4
- Spring Cloud 2023.0.3
- Spring Data JPA
- PostgreSQL 16
- Maven

### Frontend
- React 18
- Vite
- React Router
- Axios
- TailwindCSS
- Lucide Icons

### Arquitetura e Infraestrutura
- **Spring Cloud Gateway**: API Gateway
- **Netflix Eureka**: Service Discovery
- **Docker & Docker Compose**: Containerização

### Observabilidade (OTEL)
- **Spring Boot Actuator**: Métricas e health checks
- **Micrometer**: Framework de métricas
- **Prometheus**: Coleta e armazenamento de métricas
- **Grafana**: Visualização e dashboards
- **OpenTelemetry**: Padrão de observabilidade

##  Desenvolvimento Local

### Executar apenas o frontend (modo desenvolvimento)

```bash
cd frontend
npm install
npm run dev
```

O frontend estará disponível em http://localhost:5173

**Nota**: Certifique-se de que os serviços backend estão rodando via API Gateway (porta 8080)

### Executar todo o sistema com Docker

```bash
docker compose up --build
```

Acesse:
- Frontend: http://localhost:3000
- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

##  Comandos Úteis

```bash
# Parar tudo
docker compose down

# Limpar dados
docker compose down -v

# Ver logs
docker compose logs -f

# Ver logs de um serviço específico
docker compose logs -f frontend
docker compose logs -f admin-service

# Reconstruir
docker compose up --build

# Reconstruir apenas o frontend
docker compose up --build frontend
```

##  Funcionalidades do Frontend

### Dashboard
- Visão geral de todos os recursos do sistema
- Estatísticas em tempo real
- Ações rápidas para navegação

### Gerenciamento de Disciplinas
- Criar, editar e excluir disciplinas
- Visualizar lista completa de disciplinas

### Gerenciamento de Turmas
- Criar novas turmas vinculadas a disciplinas
- Adicionar alunos às turmas
- Atualizar horários das turmas
- Visualizar turmas por aluno

### Gerenciamento de Usuários
- CRUD completo de usuários
- Filtrar por tipo (Aluno, Professor, Admin)
- Badges visuais para identificação rápida

### Gerenciamento de Recursos
- Cadastrar laboratórios, periféricos, salas e equipamentos
- Controlar disponibilidade
- Filtrar por tipo de recurso

### Sistema de Reservas
- Criar reservas de recursos
- Visualizar todas as reservas
- Filtrar por status (Ativa, Cancelada, Concluída)
- Estatísticas de uso

