package com.sarc.courseclass.controller;

import com.sarc.domain.CourseClass;
import com.sarc.courseclass.dto.CourseClassDTO;
import com.sarc.courseclass.service.CourseClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-classes")
public class CourseClassController {

    private final CourseClassService service;

    public CourseClassController(CourseClassService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CourseClass>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseClass> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<CourseClass> create(@RequestBody CourseClassDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
