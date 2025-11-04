# SARC - Sistema de Alocação e Reserva de Componentes

Sistema desenvolvido com arquitetura de microserviços para gerenciamento de estudantes, disciplinas e matrículas acadêmicas.

## Grupo

- **Eduardo Martignoni**
- **Nicoli de Oliveira Siqueira**
- **Ruan da Rosa Necker**
- **Salette da Costa**
- **Vicenzo Mattos Frusciante**
- **Vitória Suelen Sampaio Graff**

## Arquitetura

O projeto utiliza arquitetura de microserviços com 3 serviços independentes que compartilham o mesmo banco de dados PostgreSQL:

### 1. **student-service** (Porta 8081)
Gerenciamento de estudantes
- Cadastrar estudante com nome e número de matrícula
- Consultar por número de matrícula
- Buscar por nome (parcial)
- Listar todos os estudantes
- Health check

### 2. **course-service** (Porta 8082)
Gerenciamento de disciplinas
- Cadastrar disciplina com código, nome e horário (A-G)
- Consultar por código da disciplina
- Consultar por nome da disciplina
- Buscar por horário (A-G)
- Atualizar horário de disciplina
- Listar todas as disciplinas
- Health check
- Nota: Uma mesma disciplina pode ter múltiplos horários

### 3. **enrollment-service** (Porta 8083)
Gerenciamento de matrículas
- Matricular estudante em disciplina
- Consultar matrículas por estudante
- Consultar matrículas por disciplina
- Listar todas as matrículas
- Cancelar matrícula

## Tecnologias

- **Java 17**
- **Spring Boot 3.3.4**
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Maven**
- **Swagger/OpenAPI 3**
- **JUnit 5 & Mockito** (testes unitários)
- **JaCoCo** (cobertura de código)

## Estrutura do Projeto

```
sarc_construcao/
├── student/                  # Microserviço de Estudantes
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/student/
│   │   │   │   ├── StudentApplication.java
│   │   │   │   ├── StudentController.java
│   │   │   │   ├── StudentService.java
│   │   │   │   ├── StudentDTO.java
│   │   │   │   ├── CreateStudentRequest.java
│   │   │   │   ├── entidade/
│   │   │   │   │   └── Student.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── StudentRepository.java
│   │   │   │   └── utils/
│   │   │   │       └── ApiResponse.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/com/student/
│   ├── Dockerfile
│   └── pom.xml
│
├── course/                   # Microserviço de Disciplinas
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/course/
│   │   │   │   ├── CourseApplication.java
│   │   │   │   ├── CourseController.java
│   │   │   │   ├── CourseService.java
│   │   │   │   ├── CourseDTO.java
│   │   │   │   ├── CreateCourseRequest.java
│   │   │   │   ├── entidade/
│   │   │   │   │   └── Course.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── CourseRepository.java
│   │   │   │   └── utils/
│   │   │   │       └── ApiResponse.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/com/course/
│   ├── Dockerfile
│   └── pom.xml
│
├── enrollment/               # Microserviço de Matrículas
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/enrollment/
│   │   │   │   ├── EnrollmentApplication.java
│   │   │   │   ├── EnrollmentController.java
│   │   │   │   ├── EnrollmentService.java
│   │   │   │   ├── EnrollmentDTO.java
│   │   │   │   ├── CreateEnrollmentRequest.java
│   │   │   │   ├── entidade/
│   │   │   │   │   └── Enrollment.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── EnrollmentRepository.java
│   │   │   │   └── utils/
│   │   │   │       └── ApiResponse.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   │       └── java/com/enrollment/
│   ├── Dockerfile
│   └── pom.xml
│
├── db/                       # Scripts do Banco de Dados
│   ├── schema.sql           # Estrutura das tabelas
│   └── seed.sql             # Dados iniciais
│
├── docker-compose.yml        # Orquestração dos serviços
├── pom.xml                   # POM principal (opcional)
└── README.md
```

## Como Executar

### Pré-requisitos
- Docker
- Docker Compose
- Java 17 (para desenvolvimento local)
- Maven (para desenvolvimento local)

### Executar com Docker Compose

```bash
# Clone o repositório
git clone https://github.com/SaletteCosta/sarc_construcao.git
cd sarc_construcao

# Construir e iniciar todos os serviços
docker-compose up --build

# Ou em modo detached (background)
docker-compose up -d --build

# Parar os serviços
docker-compose down

# Parar e remover volumes (limpa banco de dados)
docker-compose down -v
```

## Documentação da API (Swagger)

Após iniciar os serviços, acesse:

- **Student Service**: http://localhost:8081/swagger-ui.html
- **Course Service**: http://localhost:8082/swagger-ui.html
- **Enrollment Service**: http://localhost:8083/swagger-ui.html

## Executar Testes

Cada microserviço possui seus próprios testes unitários com JUnit 5 e Mockito.

```bash
# Executar testes do Student Service
cd student
mvn test

# Executar testes do Course Service
cd course
mvn test

# Executar testes do Enrollment Service
cd enrollment
mvn test

# Gerar relatório de cobertura (JaCoCo)
mvn clean test jacoco:report
# Relatório disponível em: target/site/jacoco/index.html
```

## API - Endpoints

### Student Service (Porta 8081)

#### Cadastrar Estudante
```http
POST /estudantes
Content-Type: application/json

{
  "name": "João Silva",
  "registrationNumber": "202301234"
}
```

#### Listar Todos os Estudantes
```http
GET /estudantes
```

#### Buscar por Matrícula
```http
GET /estudantes/matricula/{registrationNumber}
```
Exemplo: `GET /estudantes/matricula/202301234`

#### Buscar por Nome
```http
GET /estudantes/nome/{name}
```
Exemplo: `GET /estudantes/nome/João` (busca parcial)

#### Health Check
```http
GET /estudantes/health
```

---

### Course Service (Porta 8082)

#### Cadastrar Disciplina
```http
POST /disciplinas
Content-Type: application/json

{
  "courseCode": "MAT001",
  "courseName": "Cálculo I",
  "scheduleSlot": "A"
}
```
**Horários válidos:** A, B, C, D, E, F, G

#### Listar Todas as Disciplinas
```http
GET /disciplinas
```

#### Buscar por ID
```http
GET /disciplinas/{id}
```

#### Buscar por Código
```http
GET /disciplinas/codigo/{courseCode}
```
Exemplo: `GET /disciplinas/codigo/MAT001`

#### Buscar por Nome
```http
GET /disciplinas/nome/{courseName}
```
Exemplo: `GET /disciplinas/nome/Cálculo`

#### Buscar por Horário
```http
GET /disciplinas/horario/{scheduleSlot}
```
Exemplo: `GET /disciplinas/horario/A`

#### Buscar Horário por Código
```http
GET /disciplinas/codigo/{courseCode}/horario
```

#### Atualizar Horário
```http
PUT /disciplinas/codigo/{courseCode}/horario?scheduleSlot=B
```

#### Health Check
```http
GET /disciplinas/health
```

---

### Enrollment Service (Porta 8083)

#### Matricular Estudante
```http
POST /api/matriculas
Content-Type: application/json

{
  "studentId": 1,
  "courseId": 5
}
```

#### Listar Todas as Matrículas
```http
GET /api/matriculas
```

#### Buscar Matrículas por Estudante
```http
GET /api/matriculas/estudante/{studentId}
```

#### Buscar Matrículas por Disciplina
```http
GET /api/matriculas/disciplina/{courseId}
```

#### Cancelar Matrícula
```http
DELETE /api/matriculas/{id}
```

---

### Formato de Resposta Padrão

Todos os endpoints retornam respostas no formato:

```json
{
  "success": true,
  "message": "Mensagem descritiva",
  "data": { ... }
}
```

## Banco de Dados

### Configuração PostgreSQL

Todos os microserviços compartilham o mesmo banco de dados PostgreSQL:

| Configuração | Valor |
|-------------|-------|
| **Host** | localhost (desenvolvimento) / postgres (Docker) |
| **Porta Externa** | 5433 |
| **Porta Interna** | 5432 |
| **Database** | sarcdb |
| **Usuário** | sarcuser |
| **Senha** | sarcpass |

### Estrutura do Banco

O banco possui 3 tabelas principais:

#### 1. **students** (Estudantes)
- `id` - Chave primária (serial)
- `name` - Nome do estudante (varchar 255)
- `registration_number` - Número de matrícula único (varchar 50)
- `created_at` - Data de criação
- `updated_at` - Data de atualização

#### 2. **courses** (Disciplinas)
- `id` - Chave primária (serial)
- `course_code` - Código da disciplina (varchar 20)
- `course_name` - Nome da disciplina (varchar 255)
- `schedule_slot` - Horário (A-G)
- `created_at` - Data de criação
- `updated_at` - Data de atualização

#### 3. **enrollments** (Matrículas)
- `id` - Chave primária (serial)
- `student_id` - FK para students
- `course_id` - FK para courses
- `enrollment_date` - Data da matrícula
- `status` - Status (ACTIVE/CANCELLED)
- `created_at` - Data de criação
- `updated_at` - Data de atualização

### Scripts SQL

- **`db/schema.sql`** - Criação das tabelas, índices, constraints e triggers
- **`db/seed.sql`** - Dados iniciais (20 estudantes, 32 disciplinas, matrículas)

### Inicialização Automática

O Docker Compose executa automaticamente os scripts SQL na primeira inicialização:

```bash
docker-compose up -d
```

Os scripts são executados na ordem:
1. `schema.sql` - Cria a estrutura
2. `seed.sql` - Popula com dados de exemplo

### Acessar o Banco

```bash
# Via Docker
docker exec -it sarc-db psql -U sarcuser -d sarcdb

# Via linha de comando local (se PostgreSQL instalado)
psql -h localhost -p 5433 -U sarcuser -d sarcdb
```

### Dados de Exemplo Incluídos

**20 estudantes** com matrículas de 202301234 a 202301253  
**32 disciplinas** distribuídas em horários A-G:
   - Cálculo I (horários A, B, E)
   - Álgebra Linear (horários C, D)
   - Física I (horários A, D)
   - E mais...  
**Matrículas** de exemplo já cadastradas

### Resetar o Banco

```bash
# Parar e remover volumes (apaga todos os dados)
docker-compose down -v

# Iniciar novamente (recria com dados iniciais)
docker-compose up -d
```


## Funcionalidades Implementadas

### Funcionalidades Principais
**Cadastro de Estudantes** - Nome e número de matrícula único  
**Busca de Estudantes** - Por matrícula ou nome (busca parcial)  
**Cadastro de Disciplinas** - Código, nome e horário (A-G)  
**Múltiplos Horários** - Mesma disciplina em diferentes horários  
**Sistema de Matrículas** - Matricular estudantes em disciplinas  
**Consulta de Matrículas** - Por estudante ou por disciplina  
**Cancelamento de Matrícula** - Remoção de matrículas existentes  

### Arquitetura e Qualidade
**Microserviços** - 3 serviços independentes (Student, Course, Enrollment)  
**Docker & Docker Compose** - Containerização completa  
**PostgreSQL** - Banco de dados relacional compartilhado  
**API RESTful** - Endpoints bem definidos em português  
**Documentação Swagger/OpenAPI** - Documentação interativa em cada serviço  
**Testes Unitários** - JUnit 5 + Mockito (Controllers, Services, Entities)  
**Cobertura de Código** - JaCoCo para métricas de cobertura  
**Validação de Dados** - Jakarta Validation em todos os requests  
**Tratamento de Erros** - Respostas padronizadas com ApiResponse  
**Scripts SQL** - Schema e dados iniciais automatizados  

### Recursos Técnicos
**Health Check** - Endpoints de verificação de saúde em cada serviço  
**Índices de Banco** - Otimização de consultas  
**Triggers** - Atualização automática de timestamps  
**Constraints** - Integridade referencial e validações  
**Cascata** - Deleção em cascata para manter consistência  

## Regras de Negócio

- **Matrícula única**: Cada estudante só pode se matricular uma vez na mesma disciplina
- **Número de matrícula único**: Não podem existir dois estudantes com a mesma matrícula
- **Horários válidos**: Apenas horários de A até G são permitidos
- **Integridade referencial**: Não é possível matricular em disciplina ou estudante inexistente

