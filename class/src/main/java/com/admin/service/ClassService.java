package com.admin.service;

import com.admin.dto.ClassDTO;
import com.admin.entity.ClassEntity;
import com.admin.entity.ClassStudent;
import com.admin.repository.ClassRepository;
import com.admin.repository.ClassStudentRepository;
import com.admin.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassService {
    
    private final ClassRepository classRepository;
    private final ClassStudentRepository classStudentRepository;
    private final SubjectRepository subjectRepository;
    
    @Transactional
    public ClassDTO createClass(ClassDTO classDTO) {
        if (classRepository.existsByCode(classDTO.getCode())) {
            throw new RuntimeException("Class with code " + classDTO.getCode() + " already exists");
        }
        
        if (!subjectRepository.existsById(classDTO.getSubjectId())) {
            throw new RuntimeException("Subject not found with id: " + classDTO.getSubjectId());
        }
        
        ClassEntity classEntity = new ClassEntity();
        classEntity.setCode(classDTO.getCode());
        classEntity.setSubjectId(classDTO.getSubjectId());
        classEntity.setSchedule(classDTO.getSchedule());
        
        ClassEntity saved = classRepository.save(classEntity);
        return toDTO(saved);
    }
    
    public List<ClassDTO> getAllClasses() {
        return classRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public ClassDTO getClassByCode(String code) {
        ClassEntity classEntity = classRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Class not found with code: " + code));
        return toDTO(classEntity);
    }
    
    public String getClassSchedule(String code) {
        ClassEntity classEntity = classRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Class not found with code: " + code));
        return classEntity.getSchedule();
    }
    
    public List<ClassDTO> getClassesByStudent(Long studentId) {
        return classRepository.findByStudentId(studentId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ClassDTO> getClassesBySubject(Long subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new RuntimeException("Subject not found with id: " + subjectId);
        }
        return classRepository.findBySubjectId(subjectId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public void addStudentToClass(String classCode, Long studentId) {
        ClassEntity classEntity = classRepository.findByCode(classCode)
            .orElseThrow(() -> new RuntimeException("Class not found with code: " + classCode));
        
        if (classStudentRepository.existsByClassIdAndStudentId(classEntity.getId(), studentId)) {
            throw new RuntimeException("Student already enrolled in this class");
        }
        
        ClassStudent classStudent = new ClassStudent();
        classStudent.setClassId(classEntity.getId());
        classStudent.setStudentId(studentId);
        classStudentRepository.save(classStudent);
    }
    
    @Transactional
    public ClassDTO updateSchedule(String code, String schedule) {
        ClassEntity classEntity = classRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Class not found with code: " + code));

        classEntity.setSchedule(schedule);
        ClassEntity updated = classRepository.save(classEntity);
        return toDTO(updated);
    }

    @Transactional
    public ClassDTO updateClass(Long id, ClassDTO classDTO) {
        ClassEntity classEntity = classRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Class not found with id: " + id));

        if (!subjectRepository.existsById(classDTO.getSubjectId())) {
            throw new RuntimeException("Subject not found with id: " + classDTO.getSubjectId());
        }

        classEntity.setSubjectId(classDTO.getSubjectId());
        classEntity.setSchedule(classDTO.getSchedule());
        ClassEntity updated = classRepository.save(classEntity);
        return toDTO(updated);
    }

    @Transactional
    public void deleteClass(Long id) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found with id: " + id);
        }
        classRepository.deleteById(id);
    }

    private ClassDTO toDTO(ClassEntity classEntity) {
        return new ClassDTO(
            classEntity.getId(),
            classEntity.getCode(),
            classEntity.getSubjectId(),
            classEntity.getSchedule()
        );
    }
}
