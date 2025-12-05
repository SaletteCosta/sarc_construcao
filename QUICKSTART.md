# Guia de Início Rápido - Closed CRAS

## Começar em 3 Passos

### 1. Clone e Entre no Diretório
```bash
cd sarc_construcao
```

### 2. Inicie Todo o Sistema com Docker
```bash
docker compose up --build
```

Aguarde alguns minutos para o build e inicialização de todos os serviços.

### 3. Acesse a Aplicação

- **Frontend**: http://localhost:3000
- **Admin Service API**: http://localhost:8084
- **User Service API**: http://localhost:8085
- **PostgreSQL**: localhost:5433

## Credenciais do Banco de Dados

- **Host**: localhost:5433
- **Database**: sarcdb
- **User**: sarcuser
- **Password**: sarcpass

## Dados de Teste

O sistema vem com dados pré-populados:
- 15 Disciplinas (Cálculo, Física, Programação, etc.)
- 8 Turmas
- 10 Alunos
- 4 Professores
- 1 Administrador
- 15 Recursos (Labs, Periféricos, Salas)
- 7 Reservas de exemplo

## Desenvolvimento Local (Apenas Frontend)

Se você quiser trabalhar apenas no frontend com hot-reload:

```bash
cd frontend
npm install
npm run dev
```

O frontend estará em http://localhost:5173

**Importante**: Os serviços backend devem estar rodando (portas 8084 e 8085)

## Comandos Úteis

```bash
# Ver logs em tempo real
docker compose logs -f

# Ver logs apenas do frontend
docker compose logs -f frontend

# Parar todos os serviços
docker compose down

# Limpar volumes (reinicia banco de dados)
docker compose down -v

# Rebuildar apenas o frontend
docker compose up --build frontend
```

## Estrutura de Navegação

1. **Dashboard** - Visão geral com estatísticas
2. **Disciplinas** - Gerenciar disciplinas (MAT001, PRG001, etc.)
3. **Turmas** - Criar turmas e adicionar alunos
4. **Usuários** - Gerenciar alunos, professores e admins
5. **Recursos** - Cadastrar labs, salas e equipamentos
6. **Reservas** - Sistema de agendamento

## Testes

### Backend
```bash
# Testar Admin Service
cd class && mvn test

# Testar User Service
cd user && mvn test
```

## Troubleshooting

### Porta já em uso
Se alguma porta estiver em uso, você pode alterar no `docker-compose.yml`:
- Frontend: linha 70 (padrão 3000:80)
- Admin Service: linha 32 (padrão 8084:8081)
- User Service: linha 51 (padrão 8085:8082)
- PostgreSQL: linha 13 (padrão 5433:5432)

### Frontend não conecta ao backend
1. Verifique se os serviços backend estão rodando
2. Confira as variáveis de ambiente em `frontend/.env`
3. Teste os endpoints diretamente:
   - http://localhost:8084/subjects
   - http://localhost:8085/users

### Banco de dados não inicializa
```bash
docker compose down -v
docker compose up --build
```

## Próximos Passos

1. Explore o Dashboard em http://localhost:3000
2. Crie uma nova disciplina
3. Crie uma turma vinculada à disciplina
4. Adicione alunos à turma
5. Cadastre um novo recurso
6. Faça uma reserva

## Documentação Completa

- README principal: `/README.md`
- README do frontend: `/frontend/README.md`
- Swagger Admin: http://localhost:8084/swagger-ui.html
- Swagger User: http://localhost:8085/swagger-ui.html

## Suporte

Projeto desenvolvido por:
- Eduardo Martignoni
- Nicoli de Oliveira Siqueira
- Ruan da Rosa Necker
- Salette da Costa
- Vicenzo Mattos Frusciante
- Vitória Suelen Sampaio Graff

PUCRS - Construção de Software
