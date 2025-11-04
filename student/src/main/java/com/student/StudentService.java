package com.student;

import com.student.entidade.Student;
import com.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Transactional
    public StudentDTO createStudent(CreateStudentRequest request) {
        if (studentRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new RuntimeException("Já existe um estudante com este número de matrícula");
        }
        
        Student student = new Student();
        student.setName(request.getName());
        student.setRegistrationNumber(request.getRegistrationNumber());
        
        Student saved = studentRepository.save(student);
        return toDTO(saved);
    }
    
    public StudentDTO findByRegistrationNumber(String registrationNumber) {
        Student student = studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));
        return toDTO(student);
    }
    
    public List<StudentDTO> findByNameContaining(String name) {
        List<Student> students = studentRepository.findByNameContainingIgnoreCase(name);
        return students.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<StudentDTO> findAll() {
        return studentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setRegistrationNumber(student.getRegistrationNumber());
        return dto;
    }
}
