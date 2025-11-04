package com.admin.controller;

import com.admin.dto.SubjectDTO;
import com.admin.service.SubjectService;
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

@WebMvcTest(SubjectController.class)
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Test
    void testCreateSubject() throws Exception {
        SubjectDTO subjectDTO = new SubjectDTO(1L, "PRG001", "Programming");
        when(subjectService.createSubject(any(SubjectDTO.class))).thenReturn(subjectDTO);

        mockMvc.perform(post("/disciplinas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"PRG001\",\"name\":\"Programming\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("PRG001"));
    }

    @Test
    void testGetSubjectByCode() throws Exception {
        SubjectDTO subjectDTO = new SubjectDTO(1L, "PRG001", "Programming");
        when(subjectService.getSubjectByCode("PRG001")).thenReturn(subjectDTO);

        mockMvc.perform(get("/disciplinas/codigo/PRG001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("PRG001"));
    }

    @Test
    void testGetSubjectsByName() throws Exception {
        List<SubjectDTO> subjects = Arrays.asList(
            new SubjectDTO(1L, "PRG001", "Programming I"),
            new SubjectDTO(2L, "PRG002", "Programming II")
        );
        when(subjectService.getSubjectsByName("Programming")).thenReturn(subjects);

        mockMvc.perform(get("/disciplinas/nome/Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Programming I"));
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/disciplinas/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Subject service is healthy"));
    }
}
