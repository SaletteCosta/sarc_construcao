package com.sarc.reservation.controller;

import com.sarc.domain.Reservation;
import com.sarc.domain.ReservationStatus;
import com.sarc.reservation.dto.ReservationDTO;
import com.sarc.reservation.dto.ReservationStatusUpdateDTO;
import com.sarc.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gerenciamento de Reservas (Reservation)
 * Endpoints: /api/reservations
 */
@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservation", description = "API para gerenciamento de reservas de recursos")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    /**
     * Lista todas as reservas
     * GET /api/reservations
     */
    @Operation(
        summary = "List all reservations", 
        description = "Retorna todas as reservas cadastradas no sistema"
    )
    @GetMapping
    public ResponseEntity<List<Reservation>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Busca uma reserva por ID
     * GET /api/reservations/{id}
     */
    @Operation(
        summary = "Get reservation by ID", 
        description = "Busca uma reserva específica pelo seu ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Busca reservas por recurso
     * GET /api/reservations/resource/{resourceId}
     */
    @Operation(
        summary = "Get reservations by resource", 
        description = "Retorna todas as reservas de um recurso específico"
    )
    @GetMapping("/resource/{resourceId}")
    public ResponseEntity<List<Reservation>> getByResourceId(@PathVariable Long resourceId) {
        return ResponseEntity.ok(service.getByResourceId(resourceId));
    }

    /**
     * Busca reservas por recurso e data
     * GET /api/reservations/resource/{resourceId}/date/{date}
     */
    @Operation(
        summary = "Get reservations by resource and date", 
        description = "Retorna todas as reservas de um recurso em uma data específica"
    )
    @GetMapping("/resource/{resourceId}/date/{date}")
    public ResponseEntity<List<Reservation>> getByResourceAndDate(
            @PathVariable Long resourceId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(service.getByResourceAndDate(resourceId, date));
    }

    /**
     * Busca reservas por turma
     * GET /api/reservations/class/{classId}
     */
    @Operation(
        summary = "Get reservations by class", 
        description = "Retorna todas as reservas de uma turma específica"
    )
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Reservation>> getByClassId(@PathVariable Long classId) {
        return ResponseEntity.ok(service.getByClassId(classId));
    }

    /**
     * Busca reservas por status
     * GET /api/reservations/status/{status}
     */
    @Operation(
        summary = "Get reservations by status", 
        description = "Retorna todas as reservas com um status específico (PENDING, CONFIRMED, DENIED, DONE)"
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getByStatus(@PathVariable ReservationStatus status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    /**
     * Cria uma nova reserva
     * POST /api/reservations
     */
    @Operation(
        summary = "Create a new reservation", 
        description = "Cria uma nova reserva com validações de disponibilidade e conflitos"
    )
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Atualiza o status de uma reserva
     * PUT /api/reservations/{id}/status
     */
    @Operation(
        summary = "Update reservation status", 
        description = "Atualiza apenas o status de uma reserva existente"
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateStatus(
            @PathVariable Long id, 
            @RequestBody ReservationStatusUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.updateStatus(id, dto.getStatus()));
    }

    /**
     * Cancela uma reserva (muda status para DENIED)
     * PUT /api/reservations/{id}/cancel
     */
    @Operation(
        summary = "Cancel a reservation", 
        description = "Cancela uma reserva alterando seu status para DENIED"
    )
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    /**
     * Deleta uma reserva
     * DELETE /api/reservations/{id}
     */
    @Operation(
        summary = "Delete a reservation", 
        description = "Remove uma reserva do sistema"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
