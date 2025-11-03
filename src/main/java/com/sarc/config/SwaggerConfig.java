package com.sarc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI para documentação da API
 * Fornece interface interativa para testar endpoints
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("https://horrible-goblin-jxgqg95qqvx2qj65-8080.app.github.dev")
                                .description("GitHub Codespace API URL"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ))
                .info(new Info()
                        .title("SARC - Sistema de Alocação e Reserva de Componentes")
                        .version("1.0.0")
                        .description("""
                                API REST para gerenciamento de reservas de recursos educacionais.
                                
                                ### Funcionalidades Principais:
                                - **Gerenciamento de Usuários**: Cadastro de professores, alunos e administradores
                                - **Gerenciamento de Recursos**: Cadastro de salas, laboratórios e auditórios
                                - **Gerenciamento de Turmas**: Associação de turmas com professores
                                - **Slots de Horário**: Definição de horários disponíveis para cada recurso
                                - **Reservas**: Sistema completo de reservas com detecção automática de conflitos
                                
                                ### Validações de Reserva:
                                - Verificação de disponibilidade de horário
                                - Detecção automática de conflitos de agendamento
                                - Validação de compatibilidade entre slot de horário e recurso
                                - Validação de dia da semana
                                
                                ### Status de Reservas:
                                - **PENDING**: Aguardando aprovação
                                - **CONFIRMED**: Confirmada e ativa
                                - **DENIED**: Cancelada ou negada
                                - **DONE**: Concluída
                                """)
                        .contact(new Contact()
                                .name("Equipe SARC")
                                .email("sarc@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .tags(List.of(
                        new Tag()
                                .name("Users")
                                .description("Gerenciamento de usuários do sistema"),
                        new Tag()
                                .name("CourseClass")
                                .description("Gerenciamento de turmas e disciplinas"),
                        new Tag()
                                .name("Resources")
                                .description("Gerenciamento de recursos físicos"),
                        new Tag()
                                .name("Schedule Slots")
                                .description("Gerenciamento de horários disponíveis"),
                        new Tag()
                                .name("Reservation")
                                .description("Gerenciamento de reservas e agendamentos")
                ));
    }
}
