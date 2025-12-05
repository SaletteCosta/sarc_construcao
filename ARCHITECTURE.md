# Arquitetura do Sistema SARC

## Visão Geral

O SARC (Sistema de Agendamento de Recursos Acadêmicos) é uma aplicação de microserviços desenvolvida para gerenciar recursos acadêmicos como salas, laboratórios, equipamentos e suas reservas.

## Padrão Arquitetural

A aplicação segue o padrão de **Microserviços** com as seguintes características:

- Separação de responsabilidades por domínio de negócio
- Comunicação via API REST
- Service Discovery para localização dinâmica de serviços
- API Gateway como ponto único de entrada
- Observabilidade centralizada com Prometheus e Grafana

## Componentes Principais

### 1. Eureka Server (Porta 8761)
**Responsabilidade:** Service Discovery

- Registro e descoberta de serviços
- Health checks automáticos
- Load balancing dinâmico

### 2. API Gateway (Porta 8080)
**Responsabilidade:** Ponto único de entrada e roteamento

- Roteamento de requisições para microserviços
- CORS centralizado
- Load balancing com Ribbon
- Métricas e observabilidade

**Rotas configuradas:**
- `/api/admin/**` → Admin Service
- `/api/user/**` → User Service

### 3. Admin Service (Porta 8081)
**Responsabilidade:** Gestão de recursos acadêmicos

**Entidades:**
- `Subject` - Disciplinas
- `Class` - Turmas

**Endpoints REST:**
- `GET /subjects` - Lista disciplinas
- `POST /subjects` - Cria disciplina
- `PUT /subjects/{id}` - Atualiza disciplina
- `DELETE /subjects/{id}` - Remove disciplina
- `GET /classes` - Lista turmas
- `POST /classes` - Cria turma
- `PUT /classes/{id}` - Atualiza turma
- `DELETE /classes/{id}` - Remove turma

### 4. User Service (Porta 8082)
**Responsabilidade:** Gestão de usuários e reservas

**Entidades:**
- `User` - Usuários (STUDENT, TEACHER, ADMIN)
- `Item` - Equipamentos/Recursos (LABORATORY, CLASSROOM, EQUIPMENT, PERIPHERAL)
- `Reservation` - Reservas de recursos

**Endpoints REST:**
- `GET /users` - Lista usuários
- `POST /users` - Cria usuário
- `PUT /users/{id}` - Atualiza usuário
- `DELETE /users/{id}` - Remove usuário
- `GET /items` - Lista itens
- `POST /items` - Cria item
- `PUT /items/{id}` - Atualiza item
- `DELETE /items/{id}` - Remove item
- `GET /reservations` - Lista reservas
- `POST /reservations` - Cria reserva
- `PUT /reservations/{id}` - Atualiza reserva
- `DELETE /reservations/{id}` - Remove reserva

### 5. PostgreSQL Databases
**Responsabilidade:** Persistência de dados

- `admin_db` (Porta 5432) - Banco do Admin Service
- `user_db` (Porta 5433) - Banco do User Service

### 6. Monitoring Stack

**Prometheus (Porta 9090):**
- Coleta de métricas dos serviços
- Scraping automático via service discovery
- Armazenamento de séries temporais

**Grafana (Porta 3001):**
- Visualização de métricas
- Dashboards pré-configurados
- Alertas e monitoramento

## Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.3.4** - Framework base
- **Spring Cloud 2023.0.3** - Ferramentas para microserviços
- **Netflix Eureka** - Service Discovery
- **Spring Cloud Gateway** - API Gateway
- **Spring Data JPA** - Persistência
- **PostgreSQL 16** - Banco de dados
- **Micrometer + Prometheus** - Métricas
- **Lombok** - Redução de boilerplate

### Frontend
- **React 18** - Framework UI
- **Vite** - Build tool
- **React Router** - Navegação
- **Axios** - Cliente HTTP
- **Tailwind CSS** - Estilização
- **Lucide React** - Ícones

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração local
- **Nginx** - Servidor web do frontend

## Fluxo de Comunicação

```
Cliente → Frontend (Nginx:80)
           ↓
    API Gateway (:8080)
           ↓
    Eureka Server (:8761)
           ↓
    ┌──────┴──────┐
    ↓             ↓
Admin Service  User Service
  (:8081)        (:8082)
    ↓             ↓
 admin_db      user_db
 (:5432)       (:5433)
```

## Padrões de Design Aplicados

### 1. API Gateway Pattern
- Ponto único de entrada
- Roteamento centralizado
- CORS e segurança centralizados

### 2. Service Discovery Pattern
- Registro automático de serviços
- Descoberta dinâmica
- Health checks

### 3. Repository Pattern
- Abstração de acesso a dados
- Separação de lógica de negócio e persistência

### 4. DTO Pattern
- Transferência de dados entre camadas
- Desacoplamento de entidades

### 5. Layered Architecture
- Controller → Service → Repository
- Separação clara de responsabilidades

## Escalabilidade

O sistema permite escalabilidade horizontal:

- Múltiplas instâncias de cada microserviço
- Load balancing automático via Ribbon
- Service Discovery dinâmico
- Bancos de dados independentes por serviço

## Observabilidade

### Métricas Coletadas
- Requisições HTTP (taxa, latência, erros)
- JVM (memória, GC, threads)
- Eureka (serviços registrados, status)
- Negócio (operações CRUD)

### Dashboards
- Visão geral do sistema
- Performance por serviço
- Saúde da infraestrutura

## Segurança

Implementações atuais:
- CORS configurado no API Gateway
- Validação de dados nos endpoints
- Headers de segurança no Nginx

Melhorias futuras:
- Autenticação JWT
- Autorização baseada em roles
- Rate limiting
- HTTPS

## Deployment

### Ambiente de Desenvolvimento
```bash
docker-compose up --build
```

Todos os serviços são iniciados automaticamente com suas dependências.

### Ordem de Inicialização
1. Bancos de dados (PostgreSQL)
2. Eureka Server
3. API Gateway (aguarda Eureka)
4. Microserviços (aguardam Eureka e bancos)
5. Frontend (aguarda API Gateway)
6. Monitoring (Prometheus, Grafana)

## Referências

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Prometheus](https://prometheus.io/docs/)
- [Grafana](https://grafana.com/docs/)
