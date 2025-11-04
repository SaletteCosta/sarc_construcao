package com.enrollment;

import com.enrollment.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@Tag(name = "Matrículas", description = "API para gerenciamento de matrículas")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Efetuar matrícula", description = "Matricula um estudante em uma disciplina")
    public ResponseEntity<ApiResponse<EnrollmentDTO>> createEnrollment(@Valid @RequestBody CreateEnrollmentRequest request) {
        EnrollmentDTO enrollment = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Matrícula efetuada com sucesso", enrollment));
    }

    @GetMapping("/estudante/{studentId}")
    @Operation(summary = "Buscar por estudante", description = "Lista todas as matrículas de um estudante")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getByStudentId(@PathVariable Long studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.findByStudentId(studentId);
        String message = enrollments.isEmpty() ? "Nenhuma matrícula encontrada" : enrollments.size() + " matrícula(s) encontrada(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, enrollments));
    }

    @GetMapping("/disciplina/{courseId}")
    @Operation(summary = "Buscar por disciplina", description = "Lista todos os estudantes matriculados em uma disciplina")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getByCourseId(@PathVariable Long courseId) {
        List<EnrollmentDTO> enrollments = enrollmentService.findByCourseId(courseId);
        String message = enrollments.isEmpty() ? "Nenhuma matrícula encontrada" : enrollments.size() + " matrícula(s) encontrada(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, enrollments));
    }

    @GetMapping
    @Operation(summary = "Listar todas", description = "Lista todas as matrículas cadastradas")
    public ResponseEntity<ApiResponse<List<EnrollmentDTO>>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de matrículas", enrollments));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar matrícula", description = "Cancela uma matrícula existente")
    public ResponseEntity<ApiResponse<Void>> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Matrícula cancelada com sucesso", null));
    }
}
