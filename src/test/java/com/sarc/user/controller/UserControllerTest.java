
package com.sarc.user.controller;

import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.exception.NotFoundException;
import com.sarc.user.dto.UserDTO;
import com.sarc.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para UserController
 */
@WebMvcTest(UserController.class)
@DisplayName("UserController - Unit Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.STUDENT);
        testUser.setPassHash("default");

        testUserDTO = new UserDTO();
        testUserDTO.setName("New User");
        testUserDTO.setEmail("new@example.com");
        testUserDTO.setRole(Role.TEACHER);
    }

    @Test
    @DisplayName("GET /api/users - Deve retornar lista de usuários")
    void testListAll_Success() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/users/{id} - Deve retornar usuário por ID")
    void testGetById_Success() throws Exception {
        when(userService.getById(1L)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("GET /api/users/{id} - Deve retornar 404 quando usuário não existe")
    void testGetById_NotFound() throws Exception {
        when(userService.getById(anyLong())).thenThrow(new NotFoundException("User with ID 999 not found"));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getById(999L);
    }

    @Test
    @DisplayName("POST /api/users - Deve criar novo usuário")
    void testCreate_Success() throws Exception {
        when(userService.create(any(UserDTO.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));

        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Deve deletar usuário")
    void testDelete_Success() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Deve retornar 404 ao deletar usuário inexistente")
    void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("User with ID 999 not found")).when(userService).delete(999L);

        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).delete(999L);
    }
}
