
package com.sarc.resources.controller;

import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.exception.NotFoundException;
import com.sarc.resources.dto.ResourceDTO;
import com.sarc.resources.service.ResourceService;
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
 * Testes unitários para ResourceController
 */
@WebMvcTest(ResourceController.class)
@DisplayName("ResourceController - Unit Tests")
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceService resourceService;

    private Resource testResource;
    private ResourceDTO testDTO;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Test Lab");
        testResource.setType(ResourceType.LABORATORY);
        testResource.setCapacity(30);
        testResource.setLocalization("Building A");

        testDTO = new ResourceDTO();
        testDTO.setName("New Lab");
        testDTO.setType(ResourceType.LABORATORY);
        testDTO.setCapacity(25);
        testDTO.setLocalization("Building B");
    }

    @Test
    @DisplayName("GET /api/resources - Deve retornar lista de recursos")
    void testListAll_Success() throws Exception {
        List<Resource> resources = Arrays.asList(testResource);
        when(resourceService.getAll()).thenReturn(resources);

        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].resourceId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Lab"));

        verify(resourceService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/resources/{id} - Deve retornar recurso por ID")
    void testGetById_Success() throws Exception {
        when(resourceService.getById(1L)).thenReturn(testResource);

        mockMvc.perform(get("/api/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1))
                .andExpect(jsonPath("$.name").value("Test Lab"));

        verify(resourceService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("GET /api/resources/{id} - Deve retornar 404 quando recurso não existe")
    void testGetById_NotFound() throws Exception {
        when(resourceService.getById(anyLong())).thenThrow(new NotFoundException("Resource not found"));

        mockMvc.perform(get("/api/resources/999"))
                .andExpect(status().isNotFound());

        verify(resourceService, times(1)).getById(999L);
    }

    @Test
    @DisplayName("POST /api/resources - Deve criar novo recurso")
    void testCreate_Success() throws Exception {
        when(resourceService.create(any(ResourceDTO.class))).thenReturn(testResource);

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1));

        verify(resourceService, times(1)).create(any(ResourceDTO.class));
    }

    @Test
    @DisplayName("PUT /api/resources/{id} - Deve atualizar recurso")
    void testUpdate_Success() throws Exception {
        when(resourceService.update(anyLong(), any(ResourceDTO.class))).thenReturn(testResource);

        mockMvc.perform(put("/api/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1));

        verify(resourceService, times(1)).update(anyLong(), any(ResourceDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/resources/{id} - Deve deletar recurso")
    void testDelete_Success() throws Exception {
        doNothing().when(resourceService).delete(1L);

        mockMvc.perform(delete("/api/resources/1"))
                .andExpect(status().isNoContent());

        verify(resourceService, times(1)).delete(1L);
    }
}
