
package com.sarc.scheduleslot.controller;

import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.domain.ScheduleSlot;
import com.sarc.exception.NotFoundException;
import com.sarc.scheduleslot.dto.ScheduleSlotDTO;
import com.sarc.scheduleslot.service.ScheduleSlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para ScheduleSlotController
 */
@WebMvcTest(ScheduleSlotController.class)
@DisplayName("ScheduleSlotController - Unit Tests")
class ScheduleSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScheduleSlotService scheduleSlotService;

    private ScheduleSlot testSlot;
    private ScheduleSlotDTO testDTO;
    private Resource testResource;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Test Lab");
        testResource.setType(ResourceType.LAB);

        testSlot = new ScheduleSlot();
        testSlot.setScheduleId(1L);
        testSlot.setResource(testResource);
        testSlot.setDayOfWeek(1);
        testSlot.setStartTime(LocalTime.of(8, 0));
        testSlot.setEndTime(LocalTime.of(10, 0));

        testDTO = new ScheduleSlotDTO();
        testDTO.setResourceId(1L);
        testDTO.setDayOfWeek(2);
        testDTO.setStartTime(LocalTime.of(10, 0));
        testDTO.setEndTime(LocalTime.of(12, 0));
    }

    @Test
    @DisplayName("GET /api/schedule-slots - Deve retornar lista de slots")
    void testListAll_Success() throws Exception {
        List<ScheduleSlot> slots = Arrays.asList(testSlot);
        when(scheduleSlotService.getAll()).thenReturn(slots);

        mockMvc.perform(get("/api/schedule-slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].scheduleId").value(1))
                .andExpect(jsonPath("$[0].dayOfWeek").value(1));

        verify(scheduleSlotService, times(1)).getAll();
    }

    @Test
    @DisplayName("POST /api/schedule-slots - Deve criar novo slot")
    void testCreate_Success() throws Exception {
        when(scheduleSlotService.create(any(ScheduleSlotDTO.class))).thenReturn(testSlot);

        mockMvc.perform(post("/api/schedule-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheduleId").value(1));

        verify(scheduleSlotService, times(1)).create(any(ScheduleSlotDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/schedule-slots/{id} - Deve deletar slot")
    void testDelete_Success() throws Exception {
        doNothing().when(scheduleSlotService).delete(1L);

        mockMvc.perform(delete("/api/schedule-slots/1"))
                .andExpect(status().isNoContent());

        verify(scheduleSlotService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /api/schedule-slots/{id} - Deve retornar 404 ao deletar slot inexistente")
    void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("Schedule slot not found")).when(scheduleSlotService).delete(999L);

        mockMvc.perform(delete("/api/schedule-slots/999"))
                .andExpect(status().isNotFound());

        verify(scheduleSlotService, times(1)).delete(999L);
    }
}
