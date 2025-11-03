package com.sarc.integration;

import org.junit.jupiter.api.Disabled;

import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.repository.UserRepository;
import com.sarc.user.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para API de Usuários
 * Testa o fluxo completo da API com banco de dados real (H2)
 */
@Disabled("H2 database compatibility issue with Hibernate 6 RETURNING clause - requires PostgreSQL for integration tests")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("User API - Integration Tests")
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar, buscar, listar e deletar usuário - Fluxo completo")
    void testFullUserFlow() throws Exception {
        // 1. Criar usuário
        UserDTO createDTO = new UserDTO();
        createDTO.setName("Integration Test User");
        createDTO.setEmail("integration@test.com");
        createDTO.setRole(Role.TEACHER);

        String createResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.email").value("integration@test.com"))
                .andExpect(jsonPath("$.name").value("Integration Test User"))
                .andReturn().getResponse().getContentAsString();

        User createdUser = objectMapper.readValue(createResponse, User.class);
        Long userId = createdUser.getUserId();

        // 2. Buscar usuário por ID
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.email").value("integration@test.com"));

        // 3. Listar todos os usuários
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value(userId));

        // 4. Deletar usuário
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());

        // 5. Verificar que foi deletado
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar erro 400 ao criar usuário sem email")
    void testCreateUser_InvalidEmail() throws Exception {
        UserDTO createDTO = new UserDTO();
        createDTO.setName("Test User");
        createDTO.setEmail("");
        createDTO.setRole(Role.STUDENT);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve criar múltiplos usuários com diferentes roles")
    void testCreateMultipleUsers_DifferentRoles() throws Exception {
        // Criar TEACHER
        UserDTO teacherDTO = new UserDTO();
        teacherDTO.setName("Teacher User");
        teacherDTO.setEmail("teacher@test.com");
        teacherDTO.setRole(Role.TEACHER);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("TEACHER"));

        // Criar STUDENT
        UserDTO studentDTO = new UserDTO();
        studentDTO.setName("Student User");
        studentDTO.setEmail("student@test.com");
        studentDTO.setRole(Role.STUDENT);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("STUDENT"));

        // Criar ADMIN
        UserDTO adminDTO = new UserDTO();
        adminDTO.setName("Admin User");
        adminDTO.setEmail("admin@test.com");
        adminDTO.setRole(Role.ROOT);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));

        // Verificar que todos foram criados
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }
}
