package com.sarc.scheduleslot.service;

import com.sarc.domain.Resource;
import com.sarc.domain.ScheduleSlot;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.ResourceRepository;
import com.sarc.repository.ScheduleSlotRepository;
import com.sarc.scheduleslot.dto.ScheduleSlotDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleSlotService {

    private final ScheduleSlotRepository slotRepository;
    private final ResourceRepository resourceRepository;

    public ScheduleSlotService(ScheduleSlotRepository slotRepository, ResourceRepository resourceRepository) {
        this.slotRepository = slotRepository;
        this.resourceRepository = resourceRepository;
    }

    public List<ScheduleSlot> getAll() {
        return slotRepository.findAll();
    }

    public ScheduleSlot getById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Schedule slot with ID " + id + " not found"));
    }

    public ScheduleSlot create(ScheduleSlotDTO dto) {
        if (dto.getResourceId() == null) {
            throw new BadRequestException("Resource ID is required");
        }

        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new NotFoundException("Resource with ID " + dto.getResourceId() + " not found"));

        ScheduleSlot slot = new ScheduleSlot();
        slot.setResource(resource);
        slot.setDayOfWeek(dto.getDayOfWeek());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());

        return slotRepository.save(slot);
    }

    public ScheduleSlot update(Long id, ScheduleSlotDTO dto) {
        ScheduleSlot existing = slotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Schedule slot with ID " + id + " not found"));

        if (dto.getResourceId() != null) {
            Resource resource = resourceRepository.findById(dto.getResourceId())
                    .orElseThrow(() -> new NotFoundException("Resource with ID " + dto.getResourceId() + " not found"));
            existing.setResource(resource);
        }
        if (dto.getDayOfWeek() != null && dto.getDayOfWeek() >= 0 && dto.getDayOfWeek() <= 6) {
            existing.setDayOfWeek(dto.getDayOfWeek());
        }
        if (dto.getStartTime() != null) {
            existing.setStartTime(dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            existing.setEndTime(dto.getEndTime());
        }

        return slotRepository.save(existing);
    }

    public void delete(Long id) {
        if (!slotRepository.existsById(id)) {
            throw new NotFoundException("Schedule slot with ID " + id + " not found");
        }
        slotRepository.deleteById(id);
    }
}
