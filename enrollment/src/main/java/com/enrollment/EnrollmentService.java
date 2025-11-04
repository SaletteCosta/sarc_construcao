package com.enrollment;

import com.enrollment.entidade.Enrollment;
import com.enrollment.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Transactional
    public EnrollmentDTO createEnrollment(CreateEnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            throw new RuntimeException("Estudante já matriculado nesta disciplina");
        }
        
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(request.getStudentId());
        enrollment.setCourseId(request.getCourseId());
        
        Enrollment saved = enrollmentRepository.save(enrollment);
        return toDTO(saved);
    }
    
    public List<EnrollmentDTO> findByStudentId(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<EnrollmentDTO> findByCourseId(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<EnrollmentDTO> findAll() {
        return enrollmentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Matrícula não encontrada");
        }
        enrollmentRepository.deleteById(id);
    }
    
    private EnrollmentDTO toDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudentId());
        dto.setCourseId(enrollment.getCourseId());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        return dto;
    }
}
