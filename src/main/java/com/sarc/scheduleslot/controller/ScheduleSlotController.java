package com.sarc.scheduleslot.controller;

import com.sarc.domain.ScheduleSlot;
import com.sarc.scheduleslot.dto.ScheduleSlotDTO;
import com.sarc.scheduleslot.service.ScheduleSlotService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule-slots")
public class ScheduleSlotController {

    private final ScheduleSlotService service;

    public ScheduleSlotController(ScheduleSlotService service) {
        this.service = service;
    }

    @Operation(summary = "List all schedule slots")
    @GetMapping
    public ResponseEntity<List<ScheduleSlot>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Create a new schedule slot")
    @PostMapping
    public ResponseEntity<ScheduleSlot> create(@RequestBody ScheduleSlotDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Delete a schedule slot by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
