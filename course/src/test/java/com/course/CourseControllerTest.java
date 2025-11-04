package com.course;

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

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCourse() throws Exception {
        CreateCourseRequest request = new CreateCourseRequest("MAT001", "Matemática", "A");
        CourseDTO response = new CourseDTO(1L, "MAT001", "Matemática", "A");
        
        when(courseService.createCourse(any(CreateCourseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/disciplinas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.courseName").value("Matemática"));
    }

    @Test
    void testGetByCourseCode() throws Exception {
        List<CourseDTO> courses = Arrays.asList(
            new CourseDTO(1L, "MAT001", "Matemática", "A"),
            new CourseDTO(2L, "MAT001", "Matemática", "B")
        );
        
        when(courseService.findByCourseCode("MAT001")).thenReturn(courses);

        mockMvc.perform(get("/disciplinas/codigo/MAT001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetByScheduleSlot() throws Exception {
        List<CourseDTO> courses = Arrays.asList(
            new CourseDTO(1L, "MAT001", "Matemática", "A"),
            new CourseDTO(2L, "FIS001", "Física", "A")
        );
        
        when(courseService.findByScheduleSlot("A")).thenReturn(courses);

        mockMvc.perform(get("/disciplinas/horario/A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetById() throws Exception {
        CourseDTO response = new CourseDTO(1L, "MAT001", "Matemática", "A");
        
        when(courseService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/disciplinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.courseCode").value("MAT001"));
    }

    @Test
    void testGetAllCourses() throws Exception {
        List<CourseDTO> courses = Arrays.asList(
            new CourseDTO(1L, "MAT001", "Matemática", "A"),
            new CourseDTO(2L, "FIS001", "Física", "B")
        );
        
        when(courseService.findAll()).thenReturn(courses);

        mockMvc.perform(get("/disciplinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2));
    }
}
