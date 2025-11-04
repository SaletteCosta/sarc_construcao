package com.student;

import com.student.entidade.Student;
import com.student.repository.StudentRepository;
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

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent_Success() {
        CreateStudentRequest request = new CreateStudentRequest("João Silva", "202301234");
        Student student = new Student(1L, "João Silva", "202301234");
        
        when(studentRepository.existsByRegistrationNumber("202301234")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.createStudent(request);

        assertNotNull(result);
        assertEquals("João Silva", result.getName());
        assertEquals("202301234", result.getRegistrationNumber());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateStudent_DuplicateRegistration() {
        CreateStudentRequest request = new CreateStudentRequest("João Silva", "202301234");
        
        when(studentRepository.existsByRegistrationNumber("202301234")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> studentService.createStudent(request));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testFindByRegistrationNumber_Found() {
        Student student = new Student(1L, "João Silva", "202301234");
        
        when(studentRepository.findByRegistrationNumber("202301234")).thenReturn(Optional.of(student));

        StudentDTO result = studentService.findByRegistrationNumber("202301234");

        assertNotNull(result);
        assertEquals("João Silva", result.getName());
    }

    @Test
    void testFindByRegistrationNumber_NotFound() {
        when(studentRepository.findByRegistrationNumber("999999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.findByRegistrationNumber("999999"));
    }

    @Test
    void testFindByNameContaining() {
        List<Student> students = Arrays.asList(
            new Student(1L, "João Silva", "202301234"),
            new Student(2L, "João Santos", "202301235")
        );
        
        when(studentRepository.findByNameContainingIgnoreCase("João")).thenReturn(students);

        List<StudentDTO> result = studentService.findByNameContaining("João");

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll() {
        List<Student> students = Arrays.asList(
            new Student(1L, "João Silva", "202301234"),
            new Student(2L, "Maria Santos", "202301235")
        );
        
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDTO> result = studentService.findAll();

        assertEquals(2, result.size());
    }
}
