package com.sarc.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

/**
 * Configuração de teste padrão para todos os testes
 * Esta classe fornece beans e configurações específicas para o ambiente de teste
 */
@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {
    
    // Pode adicionar beans de teste customizados aqui se necessário
    
}
