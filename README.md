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

- **Admin Service** (8081): Gerencia turmas, disciplinas e professores
- **User Service** (8082): Gerencia usuários e reservas  
- **PostgreSQL** (5433): Banco de dados compartilhado

##  Testes

```bash
cd class && mvn test
 
cd user && mvn test
```

##  Documentação

- Swagger Admin: http://localhost:8081/swagger-ui.html
- Swagger User: http://localhost:8082/swagger-ui.html

##  Tecnologias

- Java 17
- Spring Boot 3.x
- PostgreSQL 16
- Docker & Docker Compose
- Maven

##  Comandos Úteis

```bash
# Parar tudo
docker compose down

# Limpar dados
docker compose down -v

# Ver logs
docker compose logs -f

# Reconstruir
docker compose up --build
```

