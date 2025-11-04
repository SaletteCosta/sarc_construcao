package com.course;

import com.course.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplinas")
@Tag(name = "Disciplinas", description = "API para gerenciamento de disciplinas")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    @Operation(summary = "Criar disciplina", description = "Cria uma nova disciplina com código, nome e horário")
    public ResponseEntity<ApiResponse<CourseDTO>> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        CourseDTO course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Disciplina criada com sucesso", course));
    }

    @GetMapping("/codigo/{courseCode}")
    @Operation(summary = "Buscar por código", description = "Busca disciplinas por código")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getByCourseCode(@PathVariable String courseCode) {
        List<CourseDTO> courses = courseService.findByCourseCode(courseCode);
        String message = courses.isEmpty() ? "Nenhuma disciplina encontrada" : courses.size() + " disciplina(s) encontrada(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, courses));
    }

    @GetMapping("/codigo/{courseCode}/horario")
    @Operation(summary = "Buscar horário por código", description = "Busca horário da disciplina por código")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getScheduleByCode(@PathVariable String courseCode) {
        List<CourseDTO> courses = courseService.findByCourseCode(courseCode);
        String message = courses.isEmpty() ? "Nenhum horário encontrado" : courses.size() + " horário(s) encontrado(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, courses));
    }

    @GetMapping("/nome/{courseName}")
    @Operation(summary = "Buscar por nome", description = "Busca disciplinas por nome")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getByCourseName(@PathVariable String courseName) {
        List<CourseDTO> courses = courseService.findByCourseName(courseName);
        String message = courses.isEmpty() ? "Nenhuma disciplina encontrada" : courses.size() + " disciplina(s) encontrada(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, courses));
    }

    @GetMapping("/horario/{scheduleSlot}")
    @Operation(summary = "Buscar por horário", description = "Busca disciplinas por horário")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getByScheduleSlot(@PathVariable String scheduleSlot) {
        List<CourseDTO> courses = courseService.findByScheduleSlot(scheduleSlot);
        String message = courses.isEmpty() ? "Nenhuma disciplina encontrada" : courses.size() + " disciplina(s) encontrada(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, courses));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Busca disciplina por ID")
    public ResponseEntity<ApiResponse<CourseDTO>> getById(@PathVariable Long id) {
        CourseDTO course = courseService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Disciplina encontrada", course));
    }

    @PutMapping("/codigo/{courseCode}/horario")
    @Operation(summary = "Atualizar horário", description = "Atualiza o horário da disciplina")
    public ResponseEntity<ApiResponse<CourseDTO>> updateSchedule(
            @PathVariable String courseCode,
            @RequestParam String scheduleSlot) {
        CourseDTO course = courseService.updateSchedule(courseCode, scheduleSlot);
        return ResponseEntity.ok(new ApiResponse<>(true, "Horário atualizado com sucesso", course));
    }

    @GetMapping
    @Operation(summary = "Listar todas disciplinas", description = "Retorna todas as disciplinas cadastradas")
    public ResponseEntity<ApiResponse<List<CourseDTO>>> getAllCourses() {
        List<CourseDTO> courses = courseService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de disciplinas", courses));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Health check endpoint")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Serviço de disciplinas está funcionando", "OK"));
    }
}
