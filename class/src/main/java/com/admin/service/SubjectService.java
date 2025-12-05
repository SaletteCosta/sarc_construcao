package com.admin.service;

import com.admin.dto.SubjectDTO;
import com.admin.entity.Subject;
import com.admin.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    
    private final SubjectRepository subjectRepository;
    
    @Transactional
    public SubjectDTO createSubject(SubjectDTO subjectDTO) {
        if (subjectRepository.existsByCode(subjectDTO.getCode())) {
            throw new RuntimeException("Subject with code " + subjectDTO.getCode() + " already exists");
        }
        
        Subject subject = new Subject();
        subject.setCode(subjectDTO.getCode());
        subject.setName(subjectDTO.getName());
        
        Subject saved = subjectRepository.save(subject);
        return toDTO(saved);
    }
    
    public SubjectDTO getSubjectByCode(String code) {
        Subject subject = subjectRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Subject not found with code: " + code));
        return toDTO(subject);
    }
    
    public List<SubjectDTO> getSubjectsByName(String name) {
        return subjectRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public SubjectDTO updateSubject(Long id, SubjectDTO subjectDTO) {
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        subject.setName(subjectDTO.getName());
        Subject updated = subjectRepository.save(subject);
        return toDTO(updated);
    }

    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }
        subjectRepository.deleteById(id);
    }

    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getCode(), subject.getName());
    }
}
