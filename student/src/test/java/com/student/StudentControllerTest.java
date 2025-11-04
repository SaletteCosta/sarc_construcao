package com.student;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateStudent() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest("João Silva", "202301234");
        StudentDTO response = new StudentDTO(1L, "João Silva", "202301234");
        
        when(studentService.createStudent(any(CreateStudentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("João Silva"));
    }

    @Test
    void testGetByRegistrationNumber() throws Exception {
        StudentDTO response = new StudentDTO(1L, "João Silva", "202301234");
        
        when(studentService.findByRegistrationNumber("202301234")).thenReturn(response);

        mockMvc.perform(get("/api/students/matricula/202301234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.registrationNumber").value("202301234"));
    }

    @Test
    void testGetByName() throws Exception {
        List<StudentDTO> students = Arrays.asList(
            new StudentDTO(1L, "João Silva", "202301234"),
            new StudentDTO(2L, "João Santos", "202301235")
        );
        
        when(studentService.findByNameContaining("João")).thenReturn(students);

        mockMvc.perform(get("/api/students/nome/João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetAllStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(
            new StudentDTO(1L, "João Silva", "202301234"),
            new StudentDTO(2L, "Maria Santos", "202301235")
        );
        
        when(studentService.findAll()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }
}
