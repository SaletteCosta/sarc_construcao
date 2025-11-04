package com.sarc.courseclass.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarc.courseclass.dto.CourseClassDTO;
import com.sarc.courseclass.service.CourseClassService;
import com.sarc.domain.CourseClass;
import com.sarc.domain.Role;
import com.sarc.domain.User;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseClassController.class)
class CourseClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseClassService courseClassService;

    private CourseClass testCourseClass;
    private CourseClassDTO testCourseClassDTO;
    private User teacher;

    @BeforeEach
    void setUp() {
        teacher = new User();
        teacher.setUserId(1L);
        teacher.setName("Teacher");
        teacher.setEmail("teacher@example.com");
        teacher.setRole(Role.TEACHER);

        testCourseClass = new CourseClass();
        testCourseClass.setClassId(1L);
        testCourseClass.setName("Math 101");
        testCourseClass.setTeacher(teacher);

        testCourseClassDTO = new CourseClassDTO();
        testCourseClassDTO.setName("Math 101");
        testCourseClassDTO.setTeacherId(1L);
    }

    @Test
    void listAll_ShouldReturnAllCourseClasses() throws Exception {
        CourseClass courseClass2 = new CourseClass();
        courseClass2.setClassId(2L);
        courseClass2.setName("Physics 101");
        courseClass2.setTeacher(teacher);

        List<CourseClass> courseClasses = Arrays.asList(testCourseClass, courseClass2);
        when(courseClassService.getAll()).thenReturn(courseClasses);

        mockMvc.perform(get("/api/course-classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].classId").value(1))
                .andExpect(jsonPath("$[0].name").value("Math 101"))
                .andExpect(jsonPath("$[1].classId").value(2));

        verify(courseClassService, times(1)).getAll();
    }

    @Test
    void getById_ShouldReturnCourseClass_WhenExists() throws Exception {
        when(courseClassService.getById(1L)).thenReturn(testCourseClass);

        mockMvc.perform(get("/api/course-classes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(1))
                .andExpect(jsonPath("$.name").value("Math 101"));

        verify(courseClassService, times(1)).getById(1L);
    }

    @Test
    void create_ShouldReturnCreatedCourseClass() throws Exception {
        when(courseClassService.create(any(CourseClassDTO.class))).thenReturn(testCourseClass);

        mockMvc.perform(post("/api/course-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCourseClassDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.classId").value(1))
                .andExpect(jsonPath("$.name").value("Math 101"));

        verify(courseClassService, times(1)).create(any(CourseClassDTO.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(courseClassService).delete(1L);

        mockMvc.perform(delete("/api/course-classes/1"))
                .andExpect(status().isNoContent());

        verify(courseClassService, times(1)).delete(1L);
    }
}
