
package com.sarc.reservation.controller;

import com.sarc.domain.*;
import com.sarc.exception.NotFoundException;
import com.sarc.reservation.dto.ReservationDTO;
import com.sarc.reservation.dto.ReservationStatusUpdateDTO;
import com.sarc.reservation.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para ReservationController
 */
@WebMvcTest(ReservationController.class)
@DisplayName("ReservationController - Unit Tests")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    private Reservation testReservation;
    private ReservationDTO testDTO;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 11, 4);

        CourseClass testClass = new CourseClass();
        testClass.setClassId(1L);

        Resource testResource = new Resource();
        testResource.setResourceId(1L);

        ScheduleSlot testSlot = new ScheduleSlot();
        testSlot.setScheduleId(1L);

        testReservation = new Reservation();
        testReservation.setReservationId(1L);
        testReservation.setCourseClass(testClass);
        testReservation.setResource(testResource);
        testReservation.setScheduleSlot(testSlot);
        testReservation.setReservationDate(testDate);
        testReservation.setStartTime(LocalTime.of(8, 0));
        testReservation.setEndTime(LocalTime.of(10, 0));
        testReservation.setStatus(ReservationStatus.CONFIRMED);

        testDTO = new ReservationDTO();
        testDTO.setClassId(1L);
        testDTO.setResourceId(1L);
        testDTO.setScheduleSlotId(1L);
        testDTO.setReservationDate(testDate);
        testDTO.setStartTime(LocalTime.of(10, 0));
        testDTO.setEndTime(LocalTime.of(12, 0));
        testDTO.setStatus(ReservationStatus.PENDING);
    }

    @Test
    @DisplayName("GET /api/reservations - Deve retornar lista de reservas")
    void testListAll_Success() throws Exception {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.getAll()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reservationId").value(1));

        verify(reservationService, times(1)).getAll();
    }

    @Test
    @DisplayName("GET /api/reservations/{id} - Deve retornar reserva por ID")
    void testGetById_Success() throws Exception {
        when(reservationService.getById(1L)).thenReturn(testReservation);

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1));

        verify(reservationService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("GET /api/reservations/{id} - Deve retornar 404 quando reserva não existe")
    void testGetById_NotFound() throws Exception {
        when(reservationService.getById(anyLong())).thenThrow(new NotFoundException("Reservation not found"));

        mockMvc.perform(get("/api/reservations/999"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).getById(999L);
    }

    @Test
    @DisplayName("GET /api/reservations/resource/{resourceId} - Deve retornar reservas por recurso")
    void testGetByResourceId_Success() throws Exception {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.getByResourceId(1L)).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/resource/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reservationService, times(1)).getByResourceId(1L);
    }

    @Test
    @DisplayName("GET /api/reservations/resource/{resourceId}/date/{date} - Deve retornar reservas por recurso e data")
    void testGetByResourceAndDate_Success() throws Exception {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.getByResourceAndDate(1L, testDate)).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/resource/1/date/2024-11-04"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reservationService, times(1)).getByResourceAndDate(1L, testDate);
    }

    @Test
    @DisplayName("GET /api/reservations/class/{classId} - Deve retornar reservas por turma")
    void testGetByClassId_Success() throws Exception {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.getByClassId(1L)).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/class/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reservationService, times(1)).getByClassId(1L);
    }

    @Test
    @DisplayName("GET /api/reservations/status/{status} - Deve retornar reservas por status")
    void testGetByStatus_Success() throws Exception {
        List<Reservation> reservations = Arrays.asList(testReservation);
        when(reservationService.getByStatus(ReservationStatus.CONFIRMED)).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/status/CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(reservationService, times(1)).getByStatus(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("POST /api/reservations - Deve criar nova reserva")
    void testCreate_Success() throws Exception {
        when(reservationService.create(any(ReservationDTO.class))).thenReturn(testReservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1));

        verify(reservationService, times(1)).create(any(ReservationDTO.class));
    }

    @Test
    @DisplayName("PUT /api/reservations/{id}/status - Deve atualizar status da reserva")
    void testUpdateStatus_Success() throws Exception {
        ReservationStatusUpdateDTO statusDTO = new ReservationStatusUpdateDTO();
        statusDTO.setStatus(ReservationStatus.CONFIRMED);
        
        when(reservationService.updateStatus(anyLong(), any(ReservationStatus.class))).thenReturn(testReservation);

        mockMvc.perform(put("/api/reservations/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1));

        verify(reservationService, times(1)).updateStatus(anyLong(), any(ReservationStatus.class));
    }

    @Test
    @DisplayName("PUT /api/reservations/{id}/cancel - Deve cancelar reserva")
    void testCancel_Success() throws Exception {
        testReservation.setStatus(ReservationStatus.DENIED);
        when(reservationService.cancel(1L)).thenReturn(testReservation);

        mockMvc.perform(put("/api/reservations/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1));

        verify(reservationService, times(1)).cancel(1L);
    }

    @Test
    @DisplayName("DELETE /api/reservations/{id} - Deve deletar reserva")
    void testDelete_Success() throws Exception {
        doNothing().when(reservationService).delete(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/reservations/{id} - Deve retornar 404 ao deletar reserva inexistente")
    void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("Reservation not found")).when(reservationService).delete(999L);

        mockMvc.perform(delete("/api/reservations/999"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).delete(999L);
    }
}
