package com.sarc.integration;

import com.sarc.domain.*;
import com.sarc.repository.*;
import com.sarc.resources.dto.ReservationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para API de Reservas
 * Inclui testes de detecção de conflitos com banco de dados real
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Reservation API - Integration Tests")
class ReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ScheduleSlotRepository scheduleSlotRepository;

    private User teacher;
    private CourseClass courseClass;
    private Resource resource;
    private ScheduleSlot scheduleSlot;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        // Limpar dados
        reservationRepository.deleteAll();
        scheduleSlotRepository.deleteAll();
        courseClassRepository.deleteAll();
        resourceRepository.deleteAll();
        userRepository.deleteAll();

        // Criar dados base
        testDate = LocalDate.of(2024, 11, 4); // Monday

        teacher = new User();
        teacher.setName("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setRole(Role.TEACHER);
        teacher.setPassHash("default");
        teacher = userRepository.save(teacher);

        courseClass = new CourseClass();
        courseClass.setName("Test Class");
        courseClass.setTeacher(teacher);
        courseClass = courseClassRepository.save(courseClass);

        resource = new Resource();
        resource.setName("Test Lab");
        resource.setType(ResourceType.LABORATORY);
        resource.setCapacity(30);
        resource.setLocalization("Building A");
        resource = resourceRepository.save(resource);

        scheduleSlot = new ScheduleSlot();
        scheduleSlot.setResource(resource);
        scheduleSlot.setDayOfWeek(1); // Monday
        scheduleSlot.setStartTime(LocalTime.of(8, 0));
        scheduleSlot.setEndTime(LocalTime.of(18, 0));
        scheduleSlot = scheduleSlotRepository.save(scheduleSlot);
    }

    @Test
    @DisplayName("Deve criar reserva válida sem conflitos")
    void testCreateReservation_Success() throws Exception {
        ReservationDTO dto = new ReservationDTO();
        dto.setClassId(courseClass.getClassId());
        dto.setResourceId(resource.getResourceId());
        dto.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto.setReservationDate(testDate);
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));
        dto.setStatus(ReservationStatus.PENDING);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").isNumber())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Deve detectar conflito - horários sobrepostos")
    void testCreateReservation_TimeConflict() throws Exception {
        // Criar primeira reserva (10:00-12:00)
        ReservationDTO dto1 = new ReservationDTO();
        dto1.setClassId(courseClass.getClassId());
        dto1.setResourceId(resource.getResourceId());
        dto1.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto1.setReservationDate(testDate);
        dto1.setStartTime(LocalTime.of(10, 0));
        dto1.setEndTime(LocalTime.of(12, 0));
        dto1.setStatus(ReservationStatus.CONFIRMED);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        // Tentar criar segunda reserva sobreposta (11:00-13:00)
        ReservationDTO dto2 = new ReservationDTO();
        dto2.setClassId(courseClass.getClassId());
        dto2.setResourceId(resource.getResourceId());
        dto2.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto2.setReservationDate(testDate);
        dto2.setStartTime(LocalTime.of(11, 0));
        dto2.setEndTime(LocalTime.of(13, 0));
        dto2.setStatus(ReservationStatus.PENDING);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Time conflict")));
    }

    @Test
    @DisplayName("Deve permitir reservas adjacentes (sem sobreposição)")
    void testCreateReservation_AdjacentTimesNoConflict() throws Exception {
        // Criar primeira reserva (8:00-10:00)
        ReservationDTO dto1 = new ReservationDTO();
        dto1.setClassId(courseClass.getClassId());
        dto1.setResourceId(resource.getResourceId());
        dto1.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto1.setReservationDate(testDate);
        dto1.setStartTime(LocalTime.of(8, 0));
        dto1.setEndTime(LocalTime.of(10, 0));
        dto1.setStatus(ReservationStatus.CONFIRMED);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        // Criar segunda reserva adjacente (10:00-12:00) - deve funcionar
        ReservationDTO dto2 = new ReservationDTO();
        dto2.setClassId(courseClass.getClassId());
        dto2.setResourceId(resource.getResourceId());
        dto2.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto2.setReservationDate(testDate);
        dto2.setStartTime(LocalTime.of(10, 0));
        dto2.setEndTime(LocalTime.of(12, 0));
        dto2.setStatus(ReservationStatus.PENDING);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk());

        // Verificar que ambas foram criadas
        mockMvc.perform(get("/api/reservations/resource/" + resource.getResourceId() + "/date/" + testDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Não deve detectar conflito com reservas canceladas")
    void testCreateReservation_NoConflictWithDeniedReservations() throws Exception {
        // Criar primeira reserva DENIED (10:00-12:00)
        ReservationDTO dto1 = new ReservationDTO();
        dto1.setClassId(courseClass.getClassId());
        dto1.setResourceId(resource.getResourceId());
        dto1.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto1.setReservationDate(testDate);
        dto1.setStartTime(LocalTime.of(10, 0));
        dto1.setEndTime(LocalTime.of(12, 0));
        dto1.setStatus(ReservationStatus.DENIED);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        // Criar segunda reserva no mesmo horário - deve funcionar porque a primeira está DENIED
        ReservationDTO dto2 = new ReservationDTO();
        dto2.setClassId(courseClass.getClassId());
        dto2.setResourceId(resource.getResourceId());
        dto2.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto2.setReservationDate(testDate);
        dto2.setStartTime(LocalTime.of(10, 0));
        dto2.setEndTime(LocalTime.of(12, 0));
        dto2.setStatus(ReservationStatus.CONFIRMED);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve rejeitar reserva com horário fora do schedule slot")
    void testCreateReservation_TimeOutsideSlot() throws Exception {
        ReservationDTO dto = new ReservationDTO();
        dto.setClassId(courseClass.getClassId());
        dto.setResourceId(resource.getResourceId());
        dto.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto.setReservationDate(testDate);
        dto.setStartTime(LocalTime.of(19, 0)); // Fora do slot (8:00-18:00)
        dto.setEndTime(LocalTime.of(20, 0));
        dto.setStatus(ReservationStatus.PENDING);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("outside the ScheduleSlot time range")));
    }

    @Test
    @DisplayName("Deve rejeitar reserva com dia da semana incorreto")
    void testCreateReservation_WrongDayOfWeek() throws Exception {
        LocalDate tuesday = LocalDate.of(2024, 11, 5); // Tuesday, mas slot é Monday
        
        ReservationDTO dto = new ReservationDTO();
        dto.setClassId(courseClass.getClassId());
        dto.setResourceId(resource.getResourceId());
        dto.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto.setReservationDate(tuesday);
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));
        dto.setStatus(ReservationStatus.PENDING);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("does not match ScheduleSlot day of week")));
    }

    @Test
    @DisplayName("Deve buscar reservas por diferentes filtros")
    void testGetReservations_DifferentFilters() throws Exception {
        // Criar algumas reservas
        ReservationDTO dto1 = new ReservationDTO();
        dto1.setClassId(courseClass.getClassId());
        dto1.setResourceId(resource.getResourceId());
        dto1.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto1.setReservationDate(testDate);
        dto1.setStartTime(LocalTime.of(8, 0));
        dto1.setEndTime(LocalTime.of(10, 0));
        dto1.setStatus(ReservationStatus.CONFIRMED);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1)))
                .andExpect(status().isOk());

        ReservationDTO dto2 = new ReservationDTO();
        dto2.setClassId(courseClass.getClassId());
        dto2.setResourceId(resource.getResourceId());
        dto2.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto2.setReservationDate(testDate);
        dto2.setStartTime(LocalTime.of(14, 0));
        dto2.setEndTime(LocalTime.of(16, 0));
        dto2.setStatus(ReservationStatus.PENDING);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isOk());

        // Buscar por recurso
        mockMvc.perform(get("/api/reservations/resource/" + resource.getResourceId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // Buscar por recurso e data
        mockMvc.perform(get("/api/reservations/resource/" + resource.getResourceId() + "/date/" + testDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // Buscar por turma
        mockMvc.perform(get("/api/reservations/class/" + courseClass.getClassId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // Buscar por status
        mockMvc.perform(get("/api/reservations/status/CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/reservations/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Deve atualizar status e cancelar reserva")
    void testUpdateAndCancelReservation() throws Exception {
        // Criar reserva
        ReservationDTO dto = new ReservationDTO();
        dto.setClassId(courseClass.getClassId());
        dto.setResourceId(resource.getResourceId());
        dto.setScheduleSlotId(scheduleSlot.getScheduleId());
        dto.setReservationDate(testDate);
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));
        dto.setStatus(ReservationStatus.PENDING);

        String createResponse = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Reservation created = objectMapper.readValue(createResponse, Reservation.class);
        Long reservationId = created.getReservationId();

        // Atualizar status para CONFIRMED
        String statusUpdateJson = "{\"status\":\"CONFIRMED\"}";
        mockMvc.perform(put("/api/reservations/" + reservationId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        // Cancelar reserva
        mockMvc.perform(put("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DENIED"));
    }
}
