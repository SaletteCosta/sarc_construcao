package com.sarc.user.controller;

import com.sarc.domain.User;
import com.sarc.exception.ErrorResponse;
import com.sarc.user.dto.UserDTO;
import com.sarc.user.service.UserService;
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
 * Controlador REST para gerenciamento de Usuários
 * Fornece endpoints para CRUD de usuários do sistema
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "APIs para gerenciamento de usuários do sistema (professores, alunos e administradores)")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista completa de todos os usuários cadastrados no sistema, incluindo professores, alunos e administradores"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<User>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os detalhes de um usuário específico através do seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    value = "{\"userId\": 1, \"name\": \"João Silva\", \"email\": \"joao@example.com\", \"role\": \"TEACHER\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"message\": \"User with ID 999 not found\", \"status\": 404}"
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(
        @Parameter(description = "ID do usuário", required = true, example = "1")
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
        summary = "Criar novo usuário",
        description = "Cria um novo usuário no sistema. O email é obrigatório e deve ser único. Role pode ser: TEACHER, STUDENT ou ADMIN"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - email vazio ou inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"message\": \"Email cannot be empty\", \"status\": 400}"
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<User> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do novo usuário",
            required = true,
            content = @Content(
                schema = @Schema(implementation = UserDTO.class),
                examples = @ExampleObject(
                    value = "{\"name\": \"Maria Santos\", \"email\": \"maria@example.com\", \"role\": \"STUDENT\"}"
                )
            )
        )
        @RequestBody UserDTO dto
    ) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza os dados de um usuário existente. Campos não fornecidos não serão alterados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
        @Parameter(description = "ID do usuário a ser atualizado", required = true, example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados atualizados do usuário",
            required = true,
            content = @Content(
                schema = @Schema(implementation = UserDTO.class),
                examples = @ExampleObject(
                    value = "{\"name\": \"João Silva Atualizado\", \"email\": \"joao.novo@example.com\"}"
                )
            )
        )
        @RequestBody UserDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Deletar usuário",
        description = "Remove um usuário do sistema através do seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Usuário deletado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID do usuário a ser deletado", required = true, example = "1")
        @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
