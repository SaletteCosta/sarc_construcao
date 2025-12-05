# Closed CRAS - Frontend

Interface web para o Sistema de Agendamento de Recursos Acadêmicos (SARC).

## Tecnologias Utilizadas

- **React 18** - Biblioteca JavaScript para construção de interfaces
- **Vite** - Build tool e dev server extremamente rápido
- **React Router** - Roteamento de páginas
- **Axios** - Cliente HTTP para comunicação com APIs
- **TailwindCSS** - Framework CSS utility-first
- **Lucide React** - Biblioteca de ícones

## Estrutura do Projeto

```
frontend/
├── src/
│   ├── components/
│   │   ├── common/          # Componentes reutilizáveis
│   │   │   ├── Modal.jsx
│   │   │   ├── Table.jsx
│   │   │   └── LoadingSpinner.jsx
│   │   └── layout/          # Componentes de layout
│   │       ├── Header.jsx
│   │       ├── Sidebar.jsx
│   │       └── Layout.jsx
│   ├── pages/               # Páginas da aplicação
│   │   ├── dashboard/
│   │   ├── subjects/
│   │   ├── classes/
│   │   ├── users/
│   │   ├── items/
│   │   └── reservations/
│   ├── services/            # Serviços de API
│   │   ├── api.js
│   │   ├── subjectService.js
│   │   ├── classService.js
│   │   ├── userService.js
│   │   └── reservationService.js
│   ├── App.jsx              # Componente principal
│   ├── main.jsx             # Ponto de entrada
│   └── index.css            # Estilos globais
├── public/                  # Arquivos estáticos
├── Dockerfile              # Configuração Docker
├── nginx.conf              # Configuração Nginx
└── package.json            # Dependências e scripts
```

## Instalação e Execução

### Desenvolvimento Local

1. Instalar dependências:
```bash
npm install
```

2. Configurar variáveis de ambiente:
```bash
cp .env.example .env
```

Edite o arquivo `.env` se necessário:
```env
VITE_ADMIN_SERVICE_URL=http://localhost:8084
VITE_USER_SERVICE_URL=http://localhost:8085
```

3. Executar em modo desenvolvimento:
```bash
npm run dev
```

A aplicação estará disponível em http://localhost:5173

### Build de Produção

```bash
npm run build
```

Os arquivos otimizados serão gerados na pasta `dist/`.

### Executar com Docker

```bash
docker build -t sarc-frontend .
docker run -p 3000:80 sarc-frontend
```

Ou usando docker-compose (na raiz do projeto):
```bash
docker compose up frontend
```

## Scripts Disponíveis

- `npm run dev` - Inicia servidor de desenvolvimento
- `npm run build` - Gera build de produção
- `npm run preview` - Preview do build de produção
- `npm run lint` - Executa linter

## Páginas e Funcionalidades

### Dashboard (`/`)
- Visão geral do sistema com estatísticas
- Cards com contadores de recursos
- Ações rápidas para navegação
- Informações sobre o sistema

### Disciplinas (`/subjects`)
- Listagem de todas as disciplinas
- Criar nova disciplina (código e nome)
- Editar disciplinas existentes
- Excluir disciplinas

### Turmas (`/classes`)
- Listagem de todas as turmas
- Criar nova turma vinculada a uma disciplina
- Adicionar alunos às turmas
- Atualizar horários das turmas
- Visualização do relacionamento turma-disciplina

### Usuários (`/users`)
- CRUD completo de usuários
- Filtrar por tipo (Aluno, Professor, Admin)
- Badges coloridos para identificação visual
- Validação de matrícula única

### Recursos (`/items`)
- Gerenciar laboratórios, periféricos, salas e equipamentos
- Filtrar por tipo de recurso
- Controle de disponibilidade
- Badges de status visual

### Reservas (`/reservations`)
- Criar novas reservas de recursos
- Visualizar todas as reservas
- Filtrar recursos disponíveis
- Estatísticas de reservas (ativas, concluídas, canceladas)
- Controle de status

## Componentes Principais

### Layout Components

- **Header**: Cabeçalho com logo e identificação do sistema
- **Sidebar**: Menu lateral com navegação entre páginas
- **Layout**: Container principal que engloba header, sidebar e conteúdo

### Common Components

- **Modal**: Componente modal reutilizável para formulários
- **Table**: Tabela genérica com suporte a customização de colunas
- **LoadingSpinner**: Indicador de carregamento

## Serviços de API

Todos os serviços de API utilizam Axios e estão centralizados na pasta `services/`:

- **api.js**: Configuração base do Axios com interceptors
- **subjectService.js**: Operações CRUD de disciplinas
- **classService.js**: Operações de turmas e gestão de alunos
- **userService.js**: Operações CRUD de usuários
- **reservationService.js**: Operações de reservas e itens

## Estilização

O projeto utiliza TailwindCSS com classes utilitárias customizadas definidas em `index.css`:

- `.btn-primary` - Botão primário azul
- `.btn-secondary` - Botão secundário cinza
- `.btn-danger` - Botão de perigo vermelho
- `.input-field` - Campo de input padronizado
- `.card` - Card com sombra e padding
- `.table-container` - Container para tabelas responsivas

## Cores do Tema

```js
primary: {
  50: '#eff6ff',
  100: '#dbeafe',
  200: '#bfdbfe',
  300: '#93c5fd',
  400: '#60a5fa',
  500: '#3b82f6',
  600: '#2563eb',  // Cor principal
  700: '#1d4ed8',
  800: '#1e40af',
  900: '#1e3a8a',
}
```

## Integração com Backend

O frontend se comunica com dois microsserviços:

1. **Admin Service** (porta 8084):
   - Endpoints de disciplinas (`/subjects`)
   - Endpoints de turmas (`/classes`)

2. **User Service** (porta 8085):
   - Endpoints de usuários (`/users`)
   - Endpoints de reservas (`/reservations`)
   - Endpoints de itens (`/items`)

## Deploy

### Produção com Nginx

O projeto inclui configuração Nginx otimizada com:
- Compressão Gzip
- Cache de assets estáticos
- Headers de segurança
- Fallback para React Router (SPA)

### Variáveis de Ambiente

Configure as seguintes variáveis para produção:

```env
VITE_ADMIN_SERVICE_URL=https://seu-dominio.com/api/admin
VITE_USER_SERVICE_URL=https://seu-dominio.com/api/user
```

## Requisitos

- Node.js 18+
- npm ou yarn

## Contribuidores

- Eduardo Martignoni
- Nicoli de Oliveira Siqueira
- Ruan da Rosa Necker
- Salette da Costa
- Vicenzo Mattos Frusciante
- Vitória Suelen Sampaio Graff

## Licença

Projeto acadêmico - PUCRS - Construção de Software
