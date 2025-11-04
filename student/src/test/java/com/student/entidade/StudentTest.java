package com.student.entidade;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testStudentCreation() {
        Student student = new Student();
        student.setId(1L);
        student.setName("João Silva");
        student.setRegistrationNumber("202301234");

        assertEquals(1L, student.getId());
        assertEquals("João Silva", student.getName());
        assertEquals("202301234", student.getRegistrationNumber());
    }

    @Test
    void testStudentConstructor() {
        Student student = new Student(1L, "Maria Santos", "202301235");

        assertEquals(1L, student.getId());
        assertEquals("Maria Santos", student.getName());
        assertEquals("202301235", student.getRegistrationNumber());
    }

    @Test
    void testStudentEquality() {
        Student student1 = new Student(1L, "João Silva", "202301234");
        Student student2 = new Student(1L, "João Silva", "202301234");

        assertEquals(student1, student2);
    }
}
