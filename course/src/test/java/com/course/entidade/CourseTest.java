package com.course.entidade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void testCourseCreation() {
        Course course = new Course();
        course.setId(1L);
        course.setCourseCode("MAT001");
        course.setCourseName("Matemática");
        course.setScheduleSlot("A");

        assertEquals(1L, course.getId());
        assertEquals("MAT001", course.getCourseCode());
        assertEquals("Matemática", course.getCourseName());
        assertEquals("A", course.getScheduleSlot());
    }

    @Test
    void testCourseConstructor() {
        Course course = new Course(1L, "FIS001", "Física", "B");

        assertEquals(1L, course.getId());
        assertEquals("FIS001", course.getCourseCode());
        assertEquals("Física", course.getCourseName());
        assertEquals("B", course.getScheduleSlot());
    }

    @Test
    void testCourseEquality() {
        Course course1 = new Course(1L, "MAT001", "Matemática", "A");
        Course course2 = new Course(1L, "MAT001", "Matemática", "A");

        assertEquals(course1, course2);
    }
}
