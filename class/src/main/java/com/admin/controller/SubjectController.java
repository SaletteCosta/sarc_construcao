package com.admin.controller;

import com.admin.dto.SubjectDTO;
import com.admin.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {
    
    private final SubjectService subjectService;
    
    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@RequestBody SubjectDTO subjectDTO) {
        try {
            SubjectDTO created = subjectService.createSubject(subjectDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<SubjectDTO> getSubjectByCode(@PathVariable String code) {
        try {
            SubjectDTO subject = subjectService.getSubjectByCode(code);
            return ResponseEntity.ok(subject);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByName(@PathVariable String name) {
        List<SubjectDTO> subjects = subjectService.getSubjectsByName(name);
        return ResponseEntity.ok(subjects);
    }
    
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Subject service is healthy");
    }
}
