package com.sarc.resources.controller;

import com.sarc.domain.Resource;
import com.sarc.resources.dto.ResourceDTO;
import com.sarc.resources.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @Operation(summary = "List all resources")
    @GetMapping
    public ResponseEntity<List<Resource>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Find a resource by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Resource> findById(@PathVariable Long id) {
        Resource resource = service.getById(id);
        return ResponseEntity.ok(resource);
    }

    @Operation(summary = "Create a new resource")
    @PostMapping
    public ResponseEntity<Resource> create(@RequestBody ResourceDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Update a resource by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Resource> update(@PathVariable Long id, @RequestBody ResourceDTO dto) {
        Resource updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a resource by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
