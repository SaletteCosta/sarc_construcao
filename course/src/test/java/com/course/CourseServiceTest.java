package com.course;

import com.course.entidade.Course;
import com.course.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() {
        CreateCourseRequest request = new CreateCourseRequest("MAT001", "Matemática", "A");
        Course course = new Course(1L, "MAT001", "Matemática", "A");
        
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseDTO result = courseService.createCourse(request);

        assertNotNull(result);
        assertEquals("MAT001", result.getCourseCode());
        assertEquals("Matemática", result.getCourseName());
        assertEquals("A", result.getScheduleSlot());
    }

    @Test
    void testFindByCourseCode() {
        List<Course> courses = Arrays.asList(
            new Course(1L, "MAT001", "Matemática", "A"),
            new Course(2L, "MAT001", "Matemática", "B")
        );
        
        when(courseRepository.findByCourseCode("MAT001")).thenReturn(courses);

        List<CourseDTO> result = courseService.findByCourseCode("MAT001");

        assertEquals(2, result.size());
    }

    @Test
    void testFindByScheduleSlot() {
        List<Course> courses = Arrays.asList(
            new Course(1L, "MAT001", "Matemática", "A"),
            new Course(2L, "FIS001", "Física", "A")
        );
        
        when(courseRepository.findByScheduleSlot("A")).thenReturn(courses);

        List<CourseDTO> result = courseService.findByScheduleSlot("A");

        assertEquals(2, result.size());
    }

    @Test
    void testFindById_Found() {
        Course course = new Course(1L, "MAT001", "Matemática", "A");
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDTO result = courseService.findById(1L);

        assertNotNull(result);
        assertEquals("MAT001", result.getCourseCode());
    }

    @Test
    void testFindById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.findById(999L));
    }

    @Test
    void testFindAll() {
        List<Course> courses = Arrays.asList(
            new Course(1L, "MAT001", "Matemática", "A"),
            new Course(2L, "FIS001", "Física", "B")
        );
        
        when(courseRepository.findAll()).thenReturn(courses);

        List<CourseDTO> result = courseService.findAll();

        assertEquals(2, result.size());
    }
}
