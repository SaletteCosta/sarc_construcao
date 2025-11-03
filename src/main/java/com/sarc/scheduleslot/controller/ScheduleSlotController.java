package com.sarc.scheduleslot.controller;

import com.sarc.domain.ScheduleSlot;
import com.sarc.exception.ErrorResponse;
import com.sarc.scheduleslot.dto.ScheduleSlotDTO;
import com.sarc.scheduleslot.service.ScheduleSlotService;
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
 * Controlador REST para gerenciamento de Slots de Horário
 * Define os horários disponíveis para cada recurso em cada dia da semana
 */
@RestController
@RequestMapping("/api/schedule-slots")
@Tag(name = "Schedule Slots", description = "APIs para gerenciamento de horários disponíveis dos recursos (define quando cada recurso pode ser reservado)")
public class ScheduleSlotController {

    private final ScheduleSlotService service;

    public ScheduleSlotController(ScheduleSlotService service) {
        this.service = service;
    }

    @Operation(
        summary = "Listar todos os slots de horário",
        description = "Retorna todos os slots de horário cadastrados, que definem quando cada recurso está disponível para reserva"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de slots retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ScheduleSlot.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<ScheduleSlot>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(
        summary = "Buscar slot de horário por ID",
        description = "Retorna os detalhes de um slot de horário específico através do seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Slot encontrado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ScheduleSlot.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Slot não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleSlot> getById(
        @Parameter(description = "ID do slot de horário", required = true, example = "1")
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(
        summary = "Criar novo slot de horário",
        description = "Cria um novo slot de horário para um recurso. DayOfWeek: 0=Domingo, 1=Segunda, 2=Terça, 3=Quarta, 4=Quinta, 5=Sexta, 6=Sábado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Slot criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ScheduleSlot.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos - resource ID obrigatório",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
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
    @PostMapping
    public ResponseEntity<ScheduleSlot> create(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do novo slot de horário",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ScheduleSlotDTO.class),
                examples = @ExampleObject(
                    value = "{\"resourceId\": 1, \"dayOfWeek\": 1, \"startTime\": \"08:00:00\", \"endTime\": \"18:00:00\"}"
                )
            )
        )
        @RequestBody ScheduleSlotDTO dto
    ) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(
        summary = "Atualizar slot de horário",
        description = "Atualiza os dados de um slot de horário existente. Campos não fornecidos não serão alterados."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Slot atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ScheduleSlot.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Slot não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleSlot> update(
        @Parameter(description = "ID do slot de horário", required = true, example = "1")
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados atualizados do slot",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ScheduleSlotDTO.class),
                examples = @ExampleObject(
                    value = "{\"startTime\": \"09:00:00\", \"endTime\": \"17:00:00\"}"
                )
            )
        )
        @RequestBody ScheduleSlotDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Deletar slot de horário",
        description = "Remove um slot de horário do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Slot deletado com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Slot não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @Parameter(description = "ID do slot de horário", required = true, example = "1")
        @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
