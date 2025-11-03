
package com.sarc.courseclass.controller;

import com.sarc.courseclass.dto.CourseClassDTO;
import com.sarc.courseclass.service.CourseClassService;
import com.sarc.domain.CourseClass;
import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.exception.NotFoundException;
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
 * Testes unitários para CourseClassController
 */
@WebMvcTest(CourseClassController.class)
@DisplayName("CourseClassController - Unit Tests")
class CourseClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseClassService courseClassService;

    private CourseClass testClass;
    private CourseClassDTO testDTO;
    private User testTeacher;

    @BeforeEach
    void setUp() {
        testTeacher = new User();
        testTeacher.setUserId(1L);
        testTeacher.setName("Test Teacher");
        testTeacher.setRole(Role.TEACHER);

        testClass = new CourseClass();
        testClass.setClassId(1L);
        testClass.setName("Test Class");
        testClass.setTeacher(testTeacher);

        testDTO = new CourseClassDTO();
        testDTO.setName("New Class");
        testDTO.setTeacherId(1L);
    }

    @Test
    @DisplayName("GET /api/classes - Deve retornar lista de turmas")
    void testListAll_Success() throws Exception {
        List<CourseClass> classes = Arrays.asList(testClass);
        when(courseClassService.getAll()).thenReturn(classes);

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].classId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Class"));

        verify(courseClassService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/classes/{id} - Deve retornar turma por ID")
    void testGetById_Success() throws Exception {
        when(courseClassService.getById(1L)).thenReturn(testClass);

        mockMvc.perform(get("/api/classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(1))
                .andExpect(jsonPath("$.name").value("Test Class"));

        verify(courseClassService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("GET /api/classes/{id} - Deve retornar 404 quando turma não existe")
    void testGetById_NotFound() throws Exception {
        when(courseClassService.getById(anyLong())).thenThrow(new NotFoundException("CourseClass not found"));

        mockMvc.perform(get("/api/classes/999"))
                .andExpect(status().isNotFound());

        verify(courseClassService, times(1)).getById(999L);
    }

    @Test
    @DisplayName("GET /api/classes/teacher/{teacherId} - Deve retornar turmas por professor")
    void testGetByTeacherId_Success() throws Exception {
        List<CourseClass> classes = Arrays.asList(testClass);
        when(courseClassService.getByTeacherId(1L)).thenReturn(classes);

        mockMvc.perform(get("/api/classes/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].classId").value(1));

        verify(courseClassService, times(1)).getByTeacherId(1L);
    }

    @Test
    @DisplayName("POST /api/classes - Deve criar nova turma")
    void testCreate_Success() throws Exception {
        when(courseClassService.create(any(CourseClassDTO.class))).thenReturn(testClass);

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(1));

        verify(courseClassService, times(1)).create(any(CourseClassDTO.class));
    }

    @Test
    @DisplayName("PUT /api/classes/{id} - Deve atualizar turma")
    void testUpdate_Success() throws Exception {
        when(courseClassService.update(anyLong(), any(CourseClassDTO.class))).thenReturn(testClass);

        mockMvc.perform(put("/api/classes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(1));

        verify(courseClassService, times(1)).update(anyLong(), any(CourseClassDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/classes/{id} - Deve deletar turma")
    void testDelete_Success() throws Exception {
        doNothing().when(courseClassService).delete(1L);

        mockMvc.perform(delete("/api/classes/1"))
                .andExpect(status().isNoContent());

        verify(courseClassService, times(1)).delete(1L);
    }
}
