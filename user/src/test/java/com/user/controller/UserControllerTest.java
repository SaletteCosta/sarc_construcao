package com.user.controller;

import com.user.dto.UserDTO;
import com.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "202301234", "STUDENT");
        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"registration\":\"202301234\",\"type\":\"STUDENT\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetUserByRegistration() throws Exception {
        UserDTO userDTO = new UserDTO(1L, "John Doe", "202301234", "STUDENT");
        when(userService.getUserByRegistration("202301234")).thenReturn(userDTO);

        mockMvc.perform(get("/usuarios/matricula/202301234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registration").value("202301234"));
    }

    @Test
    void testGetUsersByName() throws Exception {
        List<UserDTO> users = Arrays.asList(
            new UserDTO(1L, "John Doe", "202301234", "STUDENT"),
            new UserDTO(2L, "John Smith", "202301235", "STUDENT")
        );
        when(userService.getUsersByName("John")).thenReturn(users);

        mockMvc.perform(get("/usuarios/nome/John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(
            new UserDTO(1L, "John Doe", "202301234", "STUDENT"),
            new UserDTO(2L, "Mary Santos", "202301235", "STUDENT")
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/usuarios/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("User service is healthy"));
    }
}
