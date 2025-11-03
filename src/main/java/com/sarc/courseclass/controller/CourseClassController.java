
package com.sarc.courseclass.controller;

import com.sarc.domain.CourseClass;
import com.sarc.courseclass.dto.CourseClassDTO;
import com.sarc.courseclass.service.CourseClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de Turmas (CourseClass)
 * Endpoints: /api/classes
 */
@RestController
@RequestMapping("/api/classes")
@Tag(name = "CourseClass", description = "API para gerenciamento de turmas")
public class CourseClassController {

    private final CourseClassService service;

    public CourseClassController(CourseClassService service) {
        this.service = service;
    }

    /**
     * Lista todas as turmas
     * GET /api/classes
     */
    @Operation(summary = "List all classes", description = "Retorna todas as turmas cadastradas no sistema")
    @GetMapping
    public ResponseEntity<List<CourseClass>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Busca uma turma por ID
     * GET /api/classes/{id}
     */
    @Operation(summary = "Get class by ID", description = "Busca uma turma específica pelo seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<CourseClass> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Busca turmas de um professor específico
     * GET /api/classes/teacher/{teacherId}
     */
    @Operation(summary = "Get classes by teacher", description = "Retorna todas as turmas de um professor específico")
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CourseClass>> getByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(service.getByTeacherId(teacherId));
    }

    /**
     * Cria uma nova turma
     * POST /api/classes
     */
    @Operation(summary = "Create a new class", description = "Cria uma nova turma no sistema")
    @PostMapping
    public ResponseEntity<CourseClass> create(@RequestBody CourseClassDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Atualiza uma turma existente
     * PUT /api/classes/{id}
     */
    @Operation(summary = "Update a class", description = "Atualiza os dados de uma turma existente")
    @PutMapping("/{id}")
    public ResponseEntity<CourseClass> update(@PathVariable Long id, @RequestBody CourseClassDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Deleta uma turma
     * DELETE /api/classes/{id}
     */
    @Operation(summary = "Delete a class", description = "Remove uma turma do sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
