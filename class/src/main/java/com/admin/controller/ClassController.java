package com.admin.controller;

import com.admin.dto.ClassDTO;
import com.admin.dto.UpdateScheduleRequest;
import com.admin.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassController {
    
    private final ClassService classService;
    
    @PostMapping
    public ResponseEntity<ClassDTO> createClass(@RequestBody ClassDTO classDTO) {
        try {
            ClassDTO created = classService.createClass(classDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ClassDTO> getClassByCode(@PathVariable String code) {
        try {
            ClassDTO classDTO = classService.getClassByCode(code);
            return ResponseEntity.ok(classDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/code/{code}/schedule")
    public ResponseEntity<String> getClassSchedule(@PathVariable String code) {
        try {
            String schedule = classService.getClassSchedule(code);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ClassDTO>> getClassesByStudent(@PathVariable Long studentId) {
        List<ClassDTO> classes = classService.getClassesByStudent(studentId);
        return ResponseEntity.ok(classes);
    }
    
    @PostMapping("/code/{code}/students/{studentId}")
    public ResponseEntity<Void> addStudentToClass(
            @PathVariable String code,
            @PathVariable Long studentId) {
        try {
            classService.addStudentToClass(code, studentId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping("/code/{code}/schedule")
    public ResponseEntity<ClassDTO> updateSchedule(
            @PathVariable String code,
            @RequestBody UpdateScheduleRequest request) {
        try {
            ClassDTO updated = classService.updateSchedule(code, request.getSchedule());
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ClassDTO>> getAllClasses() {
        List<ClassDTO> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Class service is healthy");
    }
}
