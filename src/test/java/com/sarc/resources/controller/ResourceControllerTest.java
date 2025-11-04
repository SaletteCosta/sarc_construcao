package com.sarc.resources.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.resources.dto.ResourceDTO;
import com.sarc.resources.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResourceService resourceService;

    private Resource testResource;
    private ResourceDTO testResourceDTO;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Room 101");
        testResource.setType(ResourceType.ROOM);
        testResource.setCapacity(30);
        testResource.setLocalization("Building A");

        testResourceDTO = new ResourceDTO();
        testResourceDTO.setName("Room 101");
        testResourceDTO.setType(ResourceType.ROOM);
        testResourceDTO.setCapacity(30);
        testResourceDTO.setLocalization("Building A");
    }

    @Test
    void listAll_ShouldReturnAllResources() throws Exception {
        Resource resource2 = new Resource();
        resource2.setResourceId(2L);
        resource2.setName("Lab 201");
        resource2.setType(ResourceType.LAB);

        List<Resource> resources = Arrays.asList(testResource, resource2);
        when(resourceService.getAll()).thenReturn(resources);

        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].resourceId").value(1))
                .andExpect(jsonPath("$[0].name").value("Room 101"))
                .andExpect(jsonPath("$[1].resourceId").value(2));

        verify(resourceService, times(1)).getAll();
    }

    @Test
    void findById_ShouldReturnResource_WhenExists() throws Exception {
        when(resourceService.getById(1L)).thenReturn(testResource);

        mockMvc.perform(get("/api/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1))
                .andExpect(jsonPath("$.name").value("Room 101"));

        verify(resourceService, times(1)).getById(1L);
    }

    @Test
    void create_ShouldReturnCreatedResource() throws Exception {
        when(resourceService.create(any(ResourceDTO.class))).thenReturn(testResource);

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResourceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1))
                .andExpect(jsonPath("$.name").value("Room 101"));

        verify(resourceService, times(1)).create(any(ResourceDTO.class));
    }

    @Test
    void update_ShouldReturnUpdatedResource() throws Exception {
        Resource updatedResource = new Resource();
        updatedResource.setResourceId(1L);
        updatedResource.setName("Room 101 Updated");
        updatedResource.setType(ResourceType.ROOM);
        updatedResource.setCapacity(40);

        when(resourceService.update(eq(1L), any(ResourceDTO.class))).thenReturn(updatedResource);

        mockMvc.perform(put("/api/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResourceDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1))
                .andExpect(jsonPath("$.name").value("Room 101 Updated"));

        verify(resourceService, times(1)).update(eq(1L), any(ResourceDTO.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(resourceService).delete(1L);

        mockMvc.perform(delete("/api/resources/1"))
                .andExpect(status().isNoContent());

        verify(resourceService, times(1)).delete(1L);
    }
}
