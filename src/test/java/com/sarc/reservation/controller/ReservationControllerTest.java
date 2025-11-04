package com.sarc.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarc.domain.*;
import com.sarc.reservation.dto.ReservationDTO;
import com.sarc.reservation.dto.ReservationResponseDTO;
import com.sarc.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    private Reservation testReservation;
    private ReservationDTO testReservationDTO;
    private ReservationResponseDTO testReservationResponseDTO;
    private CourseClass courseClass;
    private Resource resource;
    private ScheduleSlot scheduleSlot;

    @BeforeEach
    void setUp() {
        User teacher = new User();
        teacher.setUserId(1L);
        teacher.setName("Teacher");
        teacher.setRole(Role.TEACHER);

        courseClass = new CourseClass();
        courseClass.setClassId(1L);
        courseClass.setName("Math 101");
        courseClass.setTeacher(teacher);

        resource = new Resource();
        resource.setResourceId(1L);
        resource.setName("Room 101");
        resource.setType(ResourceType.ROOM);

        scheduleSlot = new ScheduleSlot();
        scheduleSlot.setScheduleId(1L);
        scheduleSlot.setResource(resource);
        scheduleSlot.setDayOfWeek(1);
        scheduleSlot.setStartTime(LocalTime.of(9, 0));
        scheduleSlot.setEndTime(LocalTime.of(11, 0));

        testReservation = new Reservation();
        testReservation.setReservationId(1L);
        testReservation.setCourseClass(courseClass);
        testReservation.setResource(resource);
        testReservation.setScheduleSlot(scheduleSlot);
        testReservation.setReservationDate(LocalDate.of(2025, 11, 10));
        testReservation.setStartTime(LocalTime.of(9, 0));
        testReservation.setEndTime(LocalTime.of(11, 0));
        testReservation.setStatus(ReservationStatus.CONFIRMED);

        testReservationDTO = new ReservationDTO();
        testReservationDTO.setCourseClassId(1L);
        testReservationDTO.setResourceId(1L);
        testReservationDTO.setScheduleSlotId(1L);
        testReservationDTO.setReservationDate(LocalDate.of(2025, 11, 10));
        testReservationDTO.setStartTime(LocalTime.of(9, 0));
        testReservationDTO.setEndTime(LocalTime.of(11, 0));

        testReservationResponseDTO = new ReservationResponseDTO(
                1L,
                "Math 101",
                "Room 101",
                LocalDate.of(2025, 11, 10),
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                ReservationStatus.CONFIRMED
        );
    }

    @Test
    void listAll_ShouldReturnAllReservations() throws Exception {
        ReservationResponseDTO response2 = new ReservationResponseDTO(
                2L, "Physics 101", "Lab 201", 
                LocalDate.of(2025, 11, 11), 
                LocalTime.of(14, 0), 
                LocalTime.of(16, 0),
                ReservationStatus.PENDING
        );

        List<ReservationResponseDTO> reservations = Arrays.asList(testReservationResponseDTO, response2);
        when(reservationService.getAll()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].courseClassName").value("Math 101"))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(reservationService, times(1)).getAll();
    }

    @Test
    void getById_ShouldReturnReservation_WhenExists() throws Exception {
        when(reservationService.getById(1L)).thenReturn(testReservation);

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(reservationService, times(1)).getById(1L);
    }

    @Test
    void create_ShouldReturnCreatedReservation() throws Exception {
        when(reservationService.create(any(ReservationDTO.class))).thenReturn(testReservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReservationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(reservationService, times(1)).create(any(ReservationDTO.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(reservationService).delete(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).delete(1L);
    }
}
