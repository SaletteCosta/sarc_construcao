package com.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateEnrollment() throws Exception {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest(1L, 1L);
        EnrollmentDTO response = new EnrollmentDTO(1L, 1L, 1L, LocalDateTime.now());
        
        when(enrollmentService.createEnrollment(any(CreateEnrollmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.studentId").value(1));
    }

    @Test
    void testGetByStudentId() throws Exception {
        List<EnrollmentDTO> enrollments = Arrays.asList(
            new EnrollmentDTO(1L, 1L, 1L, LocalDateTime.now()),
            new EnrollmentDTO(2L, 1L, 2L, LocalDateTime.now())
        );
        
        when(enrollmentService.findByStudentId(1L)).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetByCourseId() throws Exception {
        List<EnrollmentDTO> enrollments = Arrays.asList(
            new EnrollmentDTO(1L, 1L, 1L, LocalDateTime.now()),
            new EnrollmentDTO(2L, 2L, 1L, LocalDateTime.now())
        );
        
        when(enrollmentService.findByCourseId(1L)).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetAllEnrollments() throws Exception {
        List<EnrollmentDTO> enrollments = Arrays.asList(
            new EnrollmentDTO(1L, 1L, 1L, LocalDateTime.now()),
            new EnrollmentDTO(2L, 2L, 2L, LocalDateTime.now())
        );
        
        when(enrollmentService.findAll()).thenReturn(enrollments);

        mockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        doNothing().when(enrollmentService).deleteEnrollment(1L);

        mockMvc.perform(delete("/api/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        
        verify(enrollmentService, times(1)).deleteEnrollment(1L);
    }
}
