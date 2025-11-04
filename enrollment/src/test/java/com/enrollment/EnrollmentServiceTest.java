package com.enrollment;

import com.enrollment.entidade.Enrollment;
import com.enrollment.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnrollment_Success() {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest(1L, 1L);
        Enrollment enrollment = new Enrollment(1L, 1L, 1L, LocalDateTime.now());
        
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        EnrollmentDTO result = enrollmentService.createEnrollment(request);

        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        assertEquals(1L, result.getCourseId());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testCreateEnrollment_AlreadyEnrolled() {
        CreateEnrollmentRequest request = new CreateEnrollmentRequest(1L, 1L);
        
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> enrollmentService.createEnrollment(request));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void testFindByStudentId() {
        List<Enrollment> enrollments = Arrays.asList(
            new Enrollment(1L, 1L, 1L, LocalDateTime.now()),
            new Enrollment(2L, 1L, 2L, LocalDateTime.now())
        );
        
        when(enrollmentRepository.findByStudentId(1L)).thenReturn(enrollments);

        List<EnrollmentDTO> result = enrollmentService.findByStudentId(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testFindByCourseId() {
        List<Enrollment> enrollments = Arrays.asList(
            new Enrollment(1L, 1L, 1L, LocalDateTime.now()),
            new Enrollment(2L, 2L, 1L, LocalDateTime.now())
        );
        
        when(enrollmentRepository.findByCourseId(1L)).thenReturn(enrollments);

        List<EnrollmentDTO> result = enrollmentService.findByCourseId(1L);

        assertEquals(2, result.size());
    }

    @Test
    void testFindAll() {
        List<Enrollment> enrollments = Arrays.asList(
            new Enrollment(1L, 1L, 1L, LocalDateTime.now()),
            new Enrollment(2L, 2L, 2L, LocalDateTime.now())
        );
        
        when(enrollmentRepository.findAll()).thenReturn(enrollments);

        List<EnrollmentDTO> result = enrollmentService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testDeleteEnrollment_Success() {
        when(enrollmentRepository.existsById(1L)).thenReturn(true);
        
        enrollmentService.deleteEnrollment(1L);
        
        verify(enrollmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEnrollment_NotFound() {
        when(enrollmentRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> enrollmentService.deleteEnrollment(999L));
        verify(enrollmentRepository, never()).deleteById(any());
    }
}
