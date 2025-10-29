package com.sarc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("https://horrible-goblin-jxgqg95qqvx2qj65-8080.app.github.dev")
                                .description("GitHub Codespace API URL")
                ))
                .info(new Info()
                        .title("SARC API")
                        .version("1.0")
                        .description("API documentation for the SARC system"));
    }
}
