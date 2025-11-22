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

##  Serviços

- **Frontend** (3000): Interface web React
- **Admin Service** (8084): Gerencia turmas, disciplinas e professores
- **User Service** (8085): Gerencia usuários e reservas
- **PostgreSQL** (5433): Banco de dados compartilhado

##  Testes

```bash
cd class && mvn test
 
cd user && mvn test
```

##  Documentação

- **Frontend**: http://localhost:3000
- **Swagger Admin**: http://localhost:8084/swagger-ui.html
- **Swagger User**: http://localhost:8085/swagger-ui.html

##  Tecnologias

### Backend
- Java 17
- Spring Boot 3.x
- PostgreSQL 16
- Docker & Docker Compose
- Maven

### Frontend
- React 18
- Vite
- React Router
- Axios
- TailwindCSS
- Lucide Icons

##  Desenvolvimento Local

### Executar apenas o frontend (modo desenvolvimento)

```bash
cd frontend
npm install
npm run dev
```

O frontend estará disponível em http://localhost:5173

**Nota**: Certifique-se de que os serviços backend estão rodando (porta 8084 e 8085)

### Executar todo o sistema com Docker

```bash
docker compose up --build
```

Acesse:
- Frontend: http://localhost:3000
- Admin Service: http://localhost:8084
- User Service: http://localhost:8085

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

