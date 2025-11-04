package com.enrollment.entidade;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class EnrollmentTest {

    @Test
    void testEnrollmentCreation() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudentId(1L);
        enrollment.setCourseId(1L);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        assertEquals(1L, enrollment.getId());
        assertEquals(1L, enrollment.getStudentId());
        assertEquals(1L, enrollment.getCourseId());
        assertNotNull(enrollment.getEnrollmentDate());
    }

    @Test
    void testEnrollmentConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Enrollment enrollment = new Enrollment(1L, 2L, 3L, now);

        assertEquals(1L, enrollment.getId());
        assertEquals(2L, enrollment.getStudentId());
        assertEquals(3L, enrollment.getCourseId());
        assertEquals(now, enrollment.getEnrollmentDate());
    }

    @Test
    void testEnrollmentEquality() {
        LocalDateTime now = LocalDateTime.now();
        Enrollment enrollment1 = new Enrollment(1L, 1L, 1L, now);
        Enrollment enrollment2 = new Enrollment(1L, 1L, 1L, now);

        assertEquals(enrollment1, enrollment2);
    }
}
