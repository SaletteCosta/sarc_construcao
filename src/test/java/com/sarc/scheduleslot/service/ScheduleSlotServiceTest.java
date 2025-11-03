
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
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ScheduleSlotService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ScheduleSlotService - Unit Tests")
class ScheduleSlotServiceTest {

    @Mock
    private ScheduleSlotRepository slotRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ScheduleSlotService scheduleSlotService;

    private Resource testResource;
    private ScheduleSlot testSlot;
    private ScheduleSlotDTO testDTO;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Test Lab");
        testResource.setType(ResourceType.LABORATORY);

        testSlot = new ScheduleSlot();
        testSlot.setScheduleId(1L);
        testSlot.setResource(testResource);
        testSlot.setDayOfWeek(1); // Monday
        testSlot.setStartTime(LocalTime.of(8, 0));
        testSlot.setEndTime(LocalTime.of(10, 0));

        testDTO = new ScheduleSlotDTO();
        testDTO.setResourceId(1L);
        testDTO.setDayOfWeek(2); // Tuesday
        testDTO.setStartTime(LocalTime.of(10, 0));
        testDTO.setEndTime(LocalTime.of(12, 0));
    }

    @Test
    @DisplayName("Deve retornar todos os slots de horário")
    void testGetAll_Success() {
        List<ScheduleSlot> slots = Arrays.asList(testSlot, new ScheduleSlot());
        when(slotRepository.findAll()).thenReturn(slots);

        List<ScheduleSlot> result = scheduleSlotService.getAll();

        assertThat(result).hasSize(2);
        verify(slotRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve criar slot de horário com dados válidos")
    void testCreate_Success() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(slotRepository.save(any(ScheduleSlot.class))).thenReturn(testSlot);

        ScheduleSlot result = scheduleSlotService.create(testDTO);

        assertThat(result).isNotNull();
        verify(resourceRepository, times(1)).findById(1L);
        verify(slotRepository, times(1)).save(any(ScheduleSlot.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando resource ID é nulo")
    void testCreate_NullResourceId() {
        testDTO.setResourceId(null);

        assertThatThrownBy(() -> scheduleSlotService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource ID is required");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando recurso não existe")
    void testCreate_ResourceNotFound() {
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleSlotService.create(testDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 1 not found");
    }

    @Test
    @DisplayName("Deve deletar slot existente")
    void testDelete_Success() {
        when(slotRepository.existsById(1L)).thenReturn(true);
        doNothing().when(slotRepository).deleteById(1L);

        scheduleSlotService.delete(1L);

        verify(slotRepository, times(1)).existsById(1L);
        verify(slotRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao deletar slot inexistente")
    void testDelete_NotFound() {
        when(slotRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> scheduleSlotService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Schedule slot with ID 999 not found");
    }

    @Test
    @DisplayName("Deve criar slots para diferentes dias da semana")
    void testCreate_DifferentDays() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(slotRepository.save(any(ScheduleSlot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        for (int day = 0; day <= 6; day++) {
            testDTO.setDayOfWeek(day);
            ScheduleSlot result = scheduleSlotService.create(testDTO);
            assertThat(result.getDayOfWeek()).isEqualTo(day);
        }

        verify(slotRepository, times(7)).save(any(ScheduleSlot.class));
    }
}
