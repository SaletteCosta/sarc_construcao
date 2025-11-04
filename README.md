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

### Diagrama Lógico 
<img width="1192" height="834" alt="Untitled" src="https://github.com/user-attachments/assets/4273c508-631f-46fa-a57a-d33e33b5aba6" />

## Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker e Docker Compose (para o banco de dados)

### Passos

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/SaletteCosta/sarc_construcao.git
   cd sarc_construcao
   ```

2. **Inicie o banco de dados com Docker:**
   ```bash
   docker-compose up -d
   ```

3. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse a documentação da API:**
   - Swagger UI: http://localhost:8080/swagger-ui.html

## 🧪 Testes

Execute os testes unitários:
```bash
mvn test
```

Execute os testes com relatório de cobertura:
```bash
mvn clean test jacoco:report
```

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/sarc/
│   │   ├── config/          # Configurações (CORS, Swagger)
│   │   ├── domain/          # Entidades JPA
│   │   ├── repository/      # Repositórios Spring Data
│   │   ├── courseclass/     # Módulo de Turmas
│   │   ├── reservation/     # Módulo de Reservas
│   │   ├── resources/       # Módulo de Recursos
│   │   ├── scheduleslot/    # Módulo de Horários
│   │   ├── user/            # Módulo de Usuários
│   │   ├── exception/       # Tratamento de exceções
│   │   └── web/             # Controllers gerais
│   └── resources/
│       └── application.yml  # Configurações da aplicação
└── test/                    # Testes unitários
```
