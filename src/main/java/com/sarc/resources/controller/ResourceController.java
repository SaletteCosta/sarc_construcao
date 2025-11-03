package com.sarc.resources.controller;

import com.sarc.domain.Resource;
import com.sarc.exception.ErrorResponse;
import com.sarc.resources.dto.ResourceDTO;
import com.sarc.resources.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de Recursos
 * Fornece endpoints para CRUD de recursos físicos (salas, laboratórios, auditórios)
 */
@RestController
@RequestMapping("/api/resources")
@Tag(name = "Resources", description = "APIs para gerenciamento de recursos físicos como salas de aula, laboratórios e auditórios")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @Operation(
        summary = "Listar todos os recursos",
        description = "Retorna uma lista completa de todos os recursos cadastrados no sistema (salas, laboratórios, auditórios, etc.)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de recursos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Resource.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<Resource>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
        summary = "Buscar recurso por ID",
        description = "Retorna os detalhes de um recurso específico através do seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recurso encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Resource.class),
                examples = @ExampleObject(
                    value = "{\"resourceId\": 1, \"name\": \"Lab 101\", \"type\": \"LABORATORY\", \"capacity\": 30, \"localization\": \"Building A\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Recurso não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resource> findById(
        @Parameter(description = "ID do recurso", required = true, example = "1")
        @PathVariable Long id
    ) {
        Resource resource = service.getById(id);
        return ResponseEntity.ok(resource);
    }

    @Operation(
        summary = "Criar novo recurso",
        description = "Cria um novo recurso no sistema. Tipos disponíveis: CLASSROOM, LABORATORY, AUDITORIUM, SPORTS_COURT, OTHER"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recurso criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Resource.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - nome vazio",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PostMapping
    public ResponseEntity<Resource> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do novo recurso",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ResourceDTO.class),
                examples = @ExampleObject(
                    value = "{\"name\": \"Lab 201\", \"type\": \"LABORATORY\", \"capacity\": 25, \"localization\": \"Building B - Floor 2\"}"
                )
            )
        )
        @RequestBody ResourceDTO dto
    ) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(
        summary = "Atualizar recurso",
        description = "Atualiza os dados de um recurso existente. Campos não fornecidos não serão alterados"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Recurso atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Resource.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Recurso não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Resource> update(
        @Parameter(description = "ID do recurso", required = true, example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados atualizados do recurso",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ResourceDTO.class),
                examples = @ExampleObject(
                    value = "{\"name\": \"Lab 201 Updated\", \"capacity\": 35}"
                )
            )
        )
        @RequestBody ResourceDTO dto
    ) {
        Resource updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Deletar recurso",
        description = "Remove um recurso do sistema através do seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Recurso deletado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Recurso não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID do recurso a ser deletado", required = true, example = "1")
        @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
