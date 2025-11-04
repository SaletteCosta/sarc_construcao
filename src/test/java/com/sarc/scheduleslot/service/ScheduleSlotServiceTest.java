package com.sarc.scheduleslot.service;

import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.domain.ScheduleSlot;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.ResourceRepository;
import com.sarc.repository.ScheduleSlotRepository;
import com.sarc.scheduleslot.dto.ScheduleSlotDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleSlotServiceTest {

    @Mock
    private ScheduleSlotRepository scheduleSlotRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ScheduleSlotService scheduleSlotService;

    private ScheduleSlot testScheduleSlot;
    private ScheduleSlotDTO testScheduleSlotDTO;
    private Resource testResource;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Room 101");
        testResource.setType(ResourceType.ROOM);

        testScheduleSlot = new ScheduleSlot();
        testScheduleSlot.setScheduleId(1L);
        testScheduleSlot.setResource(testResource);
        testScheduleSlot.setDayOfWeek(1);
        testScheduleSlot.setStartTime(LocalTime.of(9, 0));
        testScheduleSlot.setEndTime(LocalTime.of(11, 0));

        testScheduleSlotDTO = new ScheduleSlotDTO();
        testScheduleSlotDTO.setResourceId(1L);
        testScheduleSlotDTO.setDayOfWeek(1);
        testScheduleSlotDTO.setStartTime(LocalTime.of(9, 0));
        testScheduleSlotDTO.setEndTime(LocalTime.of(11, 0));
    }

    @Test
    void getAll_ShouldReturnAllScheduleSlots() {
        ScheduleSlot slot2 = new ScheduleSlot();
        slot2.setScheduleId(2L);
        slot2.setDayOfWeek(2);

        when(scheduleSlotRepository.findAll()).thenReturn(Arrays.asList(testScheduleSlot, slot2));

        List<ScheduleSlot> result = scheduleSlotService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDayOfWeek()).isEqualTo(1);
        assertThat(result.get(1).getDayOfWeek()).isEqualTo(2);
        verify(scheduleSlotRepository, times(1)).findAll();
    }

    @Test
    void create_ShouldCreateScheduleSlot_WhenDtoIsValid() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(scheduleSlotRepository.save(any(ScheduleSlot.class))).thenReturn(testScheduleSlot);

        ScheduleSlot result = scheduleSlotService.create(testScheduleSlotDTO);

        assertThat(result).isNotNull();
        assertThat(result.getResource()).isEqualTo(testResource);
        assertThat(result.getDayOfWeek()).isEqualTo(1);
        verify(resourceRepository, times(1)).findById(1L);
        verify(scheduleSlotRepository, times(1)).save(any(ScheduleSlot.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenResourceIdIsNull() {
        testScheduleSlotDTO.setResourceId(null);

        assertThatThrownBy(() -> scheduleSlotService.create(testScheduleSlotDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource ID is required");

        verify(scheduleSlotRepository, never()).save(any(ScheduleSlot.class));
    }

    @Test
    void create_ShouldThrowNotFoundException_WhenResourceDoesNotExist() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleSlotService.create(testScheduleSlotDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 1 not found");

        verify(resourceRepository, times(1)).findById(1L);
        verify(scheduleSlotRepository, never()).save(any(ScheduleSlot.class));
    }

    @Test
    void delete_ShouldDeleteScheduleSlot_WhenExists() {
        when(scheduleSlotRepository.existsById(1L)).thenReturn(true);
        doNothing().when(scheduleSlotRepository).deleteById(1L);

        scheduleSlotService.delete(1L);

        verify(scheduleSlotRepository, times(1)).existsById(1L);
        verify(scheduleSlotRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(scheduleSlotRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> scheduleSlotService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Schedule slot with ID 1 not found");

        verify(scheduleSlotRepository, times(1)).existsById(1L);
        verify(scheduleSlotRepository, never()).deleteById(1L);
    }
}
