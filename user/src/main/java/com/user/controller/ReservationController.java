package com.user.controller;

import com.user.dto.ItemDTO;
import com.user.dto.ReservationDTO;
import com.user.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    
    private final ReservationService reservationService;
    
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO created = reservationService.createReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    
    @PostMapping("/peripheral")
    public ResponseEntity<ReservationDTO> createPeripheralReservation(@RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO created = reservationService.createPeripheralReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ReservationDTO> getReservationByCode(@PathVariable String code) {
        try {
            ReservationDTO reservation = reservationService.getReservationByCode(code);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/schedule/{schedule}")
    public ResponseEntity<List<ReservationDTO>> getReservationsBySchedule(@PathVariable String schedule) {
        List<ReservationDTO> reservations = reservationService.getReservationsBySchedule(schedule);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/user")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUser(@RequestParam Long userId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByUser(userId);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/code/{code}/schedule")
    public ResponseEntity<String> getReservationSchedule(@PathVariable String code) {
        try {
            String schedule = reservationService.getReservationSchedule(code);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/items/type")
    public ResponseEntity<List<ItemDTO>> getItemsByType(@RequestParam String type) {
        List<ItemDTO> items = reservationService.getItemsByType(type);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/student/{registration}/laboratories")
    public ResponseEntity<List<ReservationDTO>> getLaboratoriesByStudent(@PathVariable String registration) {
        List<ReservationDTO> reservations = reservationService.getLaboratoriesByStudentRegistration(registration);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO updated = reservationService.updateReservation(id, reservationDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Reservation service is healthy");
    }
}
