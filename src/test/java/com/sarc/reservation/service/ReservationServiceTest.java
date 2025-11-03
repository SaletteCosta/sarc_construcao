package com.sarc.reservation.service;

import com.sarc.domain.*;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.*;
import com.sarc.reservation.dto.ReservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ReservationService
 * Inclui testes extensivos de detecção de conflitos
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationService - Unit Tests")
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CourseClassRepository courseClassRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ScheduleSlotRepository scheduleSlotRepository;

    @InjectMocks
    private ReservationService reservationService;

    private CourseClass testClass;
    private Resource testResource;
    private ScheduleSlot testSlot;
    private Reservation testReservation;
    private ReservationDTO testDTO;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        // Setup test date (Monday)
        testDate = LocalDate.of(2024, 11, 4); // Monday

        // Setup test class
        testClass = new CourseClass();
        testClass.setClassId(1L);
        testClass.setName("Test Class");

        // Setup test resource
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Test Lab");
        testResource.setType(ResourceType.LABORATORY);

        // Setup test schedule slot (Monday, 8:00-12:00)
        testSlot = new ScheduleSlot();
        testSlot.setScheduleId(1L);
        testSlot.setResource(testResource);
        testSlot.setDayOfWeek(1); // Monday
        testSlot.setStartTime(LocalTime.of(8, 0));
        testSlot.setEndTime(LocalTime.of(12, 0));

        // Setup test reservation
        testReservation = new Reservation();
        testReservation.setReservationId(1L);
        testReservation.setCourseClass(testClass);
        testReservation.setResource(testResource);
        testReservation.setScheduleSlot(testSlot);
        testReservation.setReservationDate(testDate);
        testReservation.setStartTime(LocalTime.of(8, 0));
        testReservation.setEndTime(LocalTime.of(10, 0));
        testReservation.setStatus(ReservationStatus.CONFIRMED);

        // Setup test DTO
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
    @DisplayName("Deve retornar todas as reservas")
    void testGetAll_Success() {
        List<Reservation> reservations = Arrays.asList(testReservation, new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.getAll();

        assertThat(result).hasSize(2);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar reserva por ID")
    void testGetById_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Reservation result = reservationService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getReservationId()).isEqualTo(1L);
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando reserva não existe")
    void testGetById_NotFound() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Reservation with ID 999 not found");
    }

    @Test
    @DisplayName("Deve retornar reservas por recurso e data")
    void testGetByResourceAndDate_Success() {
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getByResourceAndDate(1L, testDate);

        assertThat(result).hasSize(1);
        verify(reservationRepository, times(1)).findByResource_ResourceIdAndReservationDate(1L, testDate);
    }

    @Test
    @DisplayName("Deve retornar reservas por turma")
    void testGetByClassId_Success() {
        when(reservationRepository.findByCourseClass_ClassId(1L))
                .thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getByClassId(1L);

        assertThat(result).hasSize(1);
        verify(reservationRepository, times(1)).findByCourseClass_ClassId(1L);
    }

    @Test
    @DisplayName("Deve retornar reservas por status")
    void testGetByStatus_Success() {
        when(reservationRepository.findByStatus(ReservationStatus.CONFIRMED))
                .thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getByStatus(ReservationStatus.CONFIRMED);

        assertThat(result).hasSize(1);
        verify(reservationRepository, times(1)).findByStatus(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Deve retornar reservas por recurso")
    void testGetByResourceId_Success() {
        when(reservationRepository.findByResource_ResourceId(1L))
                .thenReturn(Arrays.asList(testReservation));

        List<Reservation> result = reservationService.getByResourceId(1L);

        assertThat(result).hasSize(1);
        verify(reservationRepository, times(1)).findByResource_ResourceId(1L);
    }

    @Test
    @DisplayName("Deve criar reserva válida sem conflitos")
    void testCreate_Success() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Collections.emptyList());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.create(testDTO);

        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando class ID é nulo")
    void testCreate_NullClassId() {
        testDTO.setClassId(null);

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Class ID cannot be null");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando resource ID é nulo")
    void testCreate_NullResourceId() {
        testDTO.setResourceId(null);

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource ID cannot be null");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando schedule slot ID é nulo")
    void testCreate_NullScheduleSlotId() {
        testDTO.setScheduleSlotId(null);

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("ScheduleSlot ID cannot be null");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando data de reserva é nula")
    void testCreate_NullReservationDate() {
        testDTO.setReservationDate(null);

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Reservation date cannot be null");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando hora inicial é nula")
    void testCreate_NullStartTime() {
        testDTO.setStartTime(null);

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Start time cannot be null");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando hora final é nula")
    void testCreate_NullEndTime() {
        testDTO.setEndTime(null);

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("End time cannot be null");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando turma não existe")
    void testCreate_ClassNotFound() {
        when(courseClassRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass with ID 1 not found");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando recurso não existe")
    void testCreate_ResourceNotFound() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 1 not found");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando schedule slot não existe")
    void testCreate_ScheduleSlotNotFound() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ScheduleSlot with ID 1 not found");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando slot não pertence ao recurso")
    void testCreate_SlotNotBelongToResource() {
        Resource anotherResource = new Resource();
        anotherResource.setResourceId(2L);
        testSlot.setResource(anotherResource);

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("does not belong to Resource");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando dia da semana não corresponde ao slot")
    void testCreate_WrongDayOfWeek() {
        LocalDate tuesday = LocalDate.of(2024, 11, 5); // Tuesday
        testDTO.setReservationDate(tuesday);
        testSlot.setDayOfWeek(1); // Monday

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("does not match ScheduleSlot day of week");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando horário está fora do slot")
    void testCreate_TimeOutsideSlot() {
        testDTO.setStartTime(LocalTime.of(7, 0)); // Antes do slot (8:00)
        testDTO.setEndTime(LocalTime.of(9, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("is outside the ScheduleSlot time range");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando start_time >= end_time")
    void testCreate_InvalidTimeRange() {
        testDTO.setStartTime(LocalTime.of(10, 0));
        testDTO.setEndTime(LocalTime.of(10, 0)); // Igual ao start

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Start time must be before end time");
    }

    @Test
    @DisplayName("Deve detectar conflito - sobreposição completa")
    void testCreate_ConflictCompleteOverlap() {
        // Reserva existente: 8:00-10:00
        // Nova reserva: 8:00-10:00
        testDTO.setStartTime(LocalTime.of(8, 0));
        testDTO.setEndTime(LocalTime.of(10, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Time conflict");
    }

    @Test
    @DisplayName("Deve detectar conflito - sobreposição parcial no início")
    void testCreate_ConflictPartialOverlapStart() {
        // Reserva existente: 8:00-10:00
        // Nova reserva: 9:00-11:00
        testDTO.setStartTime(LocalTime.of(9, 0));
        testDTO.setEndTime(LocalTime.of(11, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Time conflict");
    }

    @Test
    @DisplayName("Deve detectar conflito - sobreposição parcial no fim")
    void testCreate_ConflictPartialOverlapEnd() {
        // Reserva existente: 10:00-12:00
        testReservation.setStartTime(LocalTime.of(10, 0));
        testReservation.setEndTime(LocalTime.of(12, 0));
        
        // Nova reserva: 9:00-11:00
        testDTO.setStartTime(LocalTime.of(9, 0));
        testDTO.setEndTime(LocalTime.of(11, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Time conflict");
    }

    @Test
    @DisplayName("Deve detectar conflito - nova reserva contém reserva existente")
    void testCreate_ConflictContains() {
        // Reserva existente: 9:00-10:00
        testReservation.setStartTime(LocalTime.of(9, 0));
        testReservation.setEndTime(LocalTime.of(10, 0));
        
        // Nova reserva: 8:00-11:00 (contém a existente)
        testDTO.setStartTime(LocalTime.of(8, 0));
        testDTO.setEndTime(LocalTime.of(11, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));

        assertThatThrownBy(() -> reservationService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Time conflict");
    }

    @Test
    @DisplayName("Não deve detectar conflito - horários adjacentes (fim = início)")
    void testCreate_NoConflictAdjacent() {
        // Reserva existente: 8:00-10:00
        // Nova reserva: 10:00-12:00 (adjacente, não há conflito)
        testDTO.setStartTime(LocalTime.of(10, 0));
        testDTO.setEndTime(LocalTime.of(12, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.create(testDTO);

        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Não deve detectar conflito com reservas canceladas")
    void testCreate_NoConflictWithDeniedReservations() {
        testReservation.setStatus(ReservationStatus.DENIED);
        
        testDTO.setStartTime(LocalTime.of(8, 0));
        testDTO.setEndTime(LocalTime.of(10, 0));

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(testSlot));
        when(reservationRepository.findByResource_ResourceIdAndReservationDate(1L, testDate))
                .thenReturn(Arrays.asList(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.create(testDTO);

        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Deve atualizar status da reserva")
    void testUpdateStatus_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.updateStatus(1L, ReservationStatus.CONFIRMED);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando status é nulo")
    void testUpdateStatus_NullStatus() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        assertThatThrownBy(() -> reservationService.updateStatus(1L, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Status cannot be null");
    }

    @Test
    @DisplayName("Deve cancelar reserva")
    void testCancel_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.cancel(1L);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.DENIED);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Deve deletar reserva existente")
    void testDelete_Success() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.delete(1L);

        verify(reservationRepository, times(1)).existsById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao deletar reserva inexistente")
    void testDelete_NotFound() {
        when(reservationRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> reservationService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Reservation with ID 999 not found");
    }
}
