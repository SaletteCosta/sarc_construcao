package com.sarc.reservation.service;

import com.sarc.domain.*;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.*;
import com.sarc.reservation.dto.ReservationDTO;
import com.sarc.reservation.dto.ReservationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    private Reservation testReservation;
    private ReservationDTO testReservationDTO;
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
    }

    @Test
    void getAll_ShouldReturnAllReservationsAsResponseDTO() {
        Reservation reservation2 = new Reservation();
        reservation2.setReservationId(2L);
        reservation2.setCourseClass(courseClass);
        reservation2.setResource(resource);
        reservation2.setReservationDate(LocalDate.of(2025, 11, 11));
        reservation2.setStartTime(LocalTime.of(14, 0));
        reservation2.setEndTime(LocalTime.of(16, 0));
        reservation2.setStatus(ReservationStatus.PENDING);

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(testReservation, reservation2));

        List<ReservationResponseDTO> result = reservationService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getCourseClassName()).isEqualTo("Math 101");
        assertThat(result.get(0).getResourceName()).isEqualTo("Room 101");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnReservation_WhenExists() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Reservation result = reservationService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getReservationId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Reservation with ID 1 not found");

        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void create_ShouldCreateReservationWithConfirmedStatus_WhenNoOverlap() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(courseClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(scheduleSlot));
        when(reservationRepository.existsOverlappingReservation(
                any(Resource.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.create(testReservationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void create_ShouldCreateReservationWithDeniedStatus_WhenOverlapExists() {
        Reservation deniedReservation = new Reservation();
        deniedReservation.setReservationId(1L);
        deniedReservation.setCourseClass(courseClass);
        deniedReservation.setResource(resource);
        deniedReservation.setScheduleSlot(scheduleSlot);
        deniedReservation.setReservationDate(testReservationDTO.getReservationDate());
        deniedReservation.setStartTime(testReservationDTO.getStartTime());
        deniedReservation.setEndTime(testReservationDTO.getEndTime());
        deniedReservation.setStatus(ReservationStatus.DENIED);

        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(courseClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.of(scheduleSlot));
        when(reservationRepository.existsOverlappingReservation(
                any(Resource.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class)))
                .thenReturn(true);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(deniedReservation);

        Reservation result = reservationService.create(testReservationDTO);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.DENIED);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void create_ShouldThrowNotFoundException_WhenCourseClassDoesNotExist() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(testReservationDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass not found");

        verify(courseClassRepository, times(1)).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void create_ShouldThrowNotFoundException_WhenResourceDoesNotExist() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(courseClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(testReservationDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource not found");

        verify(resourceRepository, times(1)).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void create_ShouldThrowNotFoundException_WhenScheduleSlotDoesNotExist() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(courseClass));
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(scheduleSlotRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.create(testReservationDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ScheduleSlot not found");

        verify(scheduleSlotRepository, times(1)).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void delete_ShouldDeleteReservation_WhenExists() {
        when(reservationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.delete(1L);

        verify(reservationRepository, times(1)).existsById(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(reservationRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> reservationService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Reservation not found");

        verify(reservationRepository, times(1)).existsById(1L);
        verify(reservationRepository, never()).deleteById(1L);
    }
}
