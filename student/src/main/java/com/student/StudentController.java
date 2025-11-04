package com.student;

import com.student.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudantes")
@Tag(name = "Estudantes", description = "API para gerenciamento de estudantes")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    @Operation(summary = "Criar estudante", description = "Cria um novo estudante no sistema")
    public ResponseEntity<ApiResponse<StudentDTO>> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        StudentDTO student = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Estudante criado com sucesso", student));
    }

    @GetMapping("/matricula/{registrationNumber}")
    @Operation(summary = "Buscar por matrícula", description = "Busca estudante por número de matrícula")
    public ResponseEntity<ApiResponse<StudentDTO>> getByRegistrationNumber(@PathVariable String registrationNumber) {
        StudentDTO student = studentService.findByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estudante encontrado", student));
    }

    @GetMapping("/nome/{name}")
    @Operation(summary = "Buscar por nome", description = "Busca estudantes por nome parcial")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getByName(@PathVariable String name) {
        List<StudentDTO> students = studentService.findByNameContaining(name);
        String message = students.isEmpty() ? "Nenhum estudante encontrado" : students.size() + " estudante(s) encontrado(s)";
        return ResponseEntity.ok(new ApiResponse<>(true, message, students));
    }

    @GetMapping
    @Operation(summary = "Listar todos estudantes", description = "Retorna todos os estudantes cadastrados")
    public ResponseEntity<ApiResponse<List<StudentDTO>>> getAllStudents() {
        List<StudentDTO> students = studentService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de estudantes", students));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Health check endpoint")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Serviço de estudantes está funcionando", "OK"));
    }
}
