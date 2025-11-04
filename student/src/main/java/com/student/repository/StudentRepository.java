package com.student.repository;

import com.student.entidade.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRegistrationNumber(String registrationNumber);
    List<Student> findByNameContainingIgnoreCase(String name);
    boolean existsByRegistrationNumber(String registrationNumber);
}
