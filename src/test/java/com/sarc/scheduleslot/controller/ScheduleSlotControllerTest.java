package com.sarc.scheduleslot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.domain.ScheduleSlot;
import com.sarc.scheduleslot.dto.ScheduleSlotDTO;
import com.sarc.scheduleslot.service.ScheduleSlotService;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(ScheduleSlotController.class)
class ScheduleSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void listAll_ShouldReturnAllScheduleSlots() throws Exception {
        ScheduleSlot slot2 = new ScheduleSlot();
        slot2.setScheduleId(2L);
        slot2.setDayOfWeek(2);

        List<ScheduleSlot> slots = Arrays.asList(testScheduleSlot, slot2);
        when(scheduleSlotService.getAll()).thenReturn(slots);

        mockMvc.perform(get("/api/schedule-slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].scheduleId").value(1))
                .andExpect(jsonPath("$[0].dayOfWeek").value(1))
                .andExpect(jsonPath("$[1].scheduleId").value(2));

        verify(scheduleSlotService, times(1)).getAll();
    }

    @Test
    void create_ShouldReturnCreatedScheduleSlot() throws Exception {
        when(scheduleSlotService.create(any(ScheduleSlotDTO.class))).thenReturn(testScheduleSlot);

        mockMvc.perform(post("/api/schedule-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testScheduleSlotDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheduleId").value(1))
                .andExpect(jsonPath("$.dayOfWeek").value(1));

        verify(scheduleSlotService, times(1)).create(any(ScheduleSlotDTO.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(scheduleSlotService).delete(1L);

        mockMvc.perform(delete("/api/schedule-slots/1"))
                .andExpect(status().isNoContent());

        verify(scheduleSlotService, times(1)).delete(1L);
    }
}
