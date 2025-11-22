# Arquitetura do Sistema Closed CRAS

## Visão Geral

```
┌─────────────────────────────────────────────────────────────┐
│                         FRONTEND                            │
│                    React + TailwindCSS                      │
│                     (Port: 3000)                            │
└─────────────────┬───────────────────┬───────────────────────┘
                  │                   │
                  │                   │
         ┌────────▼────────┐  ┌──────▼──────────┐
         │  Admin Service  │  │  User Service   │
         │   Spring Boot   │  │  Spring Boot    │
         │   (Port: 8084)  │  │  (Port: 8085)   │
         └────────┬────────┘  └──────┬──────────┘
                  │                  │
                  └────────┬─────────┘
                           │
                  ┌────────▼─────────┐
                  │   PostgreSQL 16  │
                  │   (Port: 5433)   │
                  └──────────────────┘
```

## Componentes

### 1. Frontend (React)

**Tecnologias**: React 18, Vite, TailwindCSS, React Router, Axios

**Páginas**:
- Dashboard - Visão geral e estatísticas
- Subjects - Gerenciamento de disciplinas
- Classes - Gerenciamento de turmas
- Users - Gerenciamento de usuários
- Items - Gerenciamento de recursos
- Reservations - Sistema de reservas

**Componentes**:
- Layout (Header, Sidebar)
- Common (Modal, Table, LoadingSpinner)

**Serviços**:
- API Client (Axios)
- Subject Service
- Class Service
- User Service
- Reservation Service

### 2. Admin Service (Spring Boot)

**Responsabilidades**:
- Gerenciar disciplinas (CRUD)
- Gerenciar turmas (CRUD)
- Relacionar alunos com turmas
- Atualizar horários

**Endpoints**:
```
GET    /subjects
POST   /subjects
GET    /subjects/code/{code}
PUT    /subjects/{id}
DELETE /subjects/{id}

GET    /classes
POST   /classes
GET    /classes/code/{code}
GET    /classes/student/{studentId}
POST   /classes/code/{code}/students/{studentId}
POST   /classes/code/{code}/schedule
GET    /classes/code/{code}/schedule
```

**Entidades**:
- Subject
- ClassEntity
- ClassStudent

### 3. User Service (Spring Boot)

**Responsabilidades**:
- Gerenciar usuários (CRUD)
- Gerenciar itens/recursos (CRUD)
- Gerenciar reservas (CRUD)
- Filtrar recursos por tipo
- Consultar reservas por usuário/horário

**Endpoints**:
```
GET    /users
POST   /users
GET    /users/registration/{registration}
GET    /users/type/{type}
PUT    /users/{id}
DELETE /users/{id}

GET    /items
POST   /items
GET    /items/code/{code}
PUT    /items/{id}
DELETE /items/{id}

GET    /reservations
POST   /reservations
POST   /reservations/peripheral
GET    /reservations/code/{code}
GET    /reservations/schedule/{schedule}
GET    /reservations/user?userId={id}
GET    /reservations/items/type?type={type}
GET    /reservations/student/{registration}/laboratories
```

**Entidades**:
- User
- Item
- Reservation

### 4. PostgreSQL Database

**Tabelas**:
- subjects - Disciplinas
- classes - Turmas
- users - Usuários (alunos, professores, admin)
- class_students - Relação N:N entre turmas e alunos
- items - Recursos (labs, periféricos, salas, equipamentos)
- reservations - Reservas de recursos
- schedules - Horários disponíveis

**Features**:
- Foreign Keys com CASCADE
- Índices otimizados
- Triggers para updated_at
- Constraints de integridade

## Fluxo de Dados

### Criar uma Reserva

```
1. Usuário acessa frontend (localhost:3000/reservations)
2. Frontend busca lista de usuários (User Service)
3. Frontend busca lista de itens disponíveis (User Service)
4. Usuário preenche formulário
5. Frontend envia POST /reservations (User Service)
6. User Service valida e salva no PostgreSQL
7. Frontend atualiza lista de reservas
```

### Adicionar Aluno a uma Turma

```
1. Usuário acessa frontend (localhost:3000/classes)
2. Frontend busca turmas (Admin Service)
3. Frontend busca alunos (User Service)
4. Usuário seleciona turma e aluno
5. Frontend envia POST /classes/code/{code}/students/{id} (Admin Service)
6. Admin Service cria registro em class_students (PostgreSQL)
7. Frontend confirma sucesso
```

## Padrões de Design

### Backend
- **Repository Pattern**: Acesso a dados
- **Service Layer**: Lógica de negócio
- **DTO Pattern**: Transferência de dados
- **RESTful API**: Comunicação HTTP

### Frontend
- **Component-Based**: React components
- **Service Layer**: API services
- **Container/Presentational**: Separação de lógica
- **Controlled Components**: Forms

## Segurança

### Backend
- CORS configurado
- Validation de inputs
- Error handling

### Frontend
- Environment variables para URLs
- Client-side validation
- Error boundaries

### Database
- Constraints de integridade
- Foreign keys com CASCADE
- Unique constraints

## Performance

### Backend
- Connection pooling (HikariCP)
- Lazy loading (JPA)
- Índices em queries frequentes

### Frontend
- Code splitting (Vite)
- Lazy loading de rotas
- React optimizations

### Database
- Índices em colunas de busca
- Views para queries complexas
- Connection pooling

## Escalabilidade

### Horizontal
- Frontend: Múltiplas instâncias Nginx
- Backend: Stateless services (fácil replicação)
- Database: Read replicas (potencial)

### Vertical
- Aumentar recursos dos containers
- Otimizar queries do banco
- Cache em memória (futuro)

## Monitoramento

### Docker
```bash
docker compose logs -f
docker compose ps
docker stats
```

### Health Checks
- Admin Service: /health
- User Service: /health
- PostgreSQL: pg_isready

## Deployment

### Desenvolvimento
```bash
docker compose up --build
```

### Produção
1. Build das imagens
2. Push para registry
3. Deploy em cluster (Kubernetes/Docker Swarm)
4. Configure load balancer
5. Setup SSL/TLS
6. Configure backup do banco

## Tecnologias e Versões

| Componente      | Tecnologia        | Versão |
|-----------------|-------------------|--------|
| Frontend        | React             | 18.x   |
| Build Tool      | Vite              | 5.x    |
| Styling         | TailwindCSS       | 3.x    |
| Backend         | Spring Boot       | 3.x    |
| Java            | OpenJDK           | 17     |
| Database        | PostgreSQL        | 16     |
| Web Server      | Nginx             | Alpine |
| Container       | Docker            | 24.x   |
| Orchestration   | Docker Compose    | 3.8    |


