package com.sarc.reservation.service;

import com.sarc.domain.*;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.*;
import com.sarc.reservation.dto.ReservationResponseDTO;
import com.sarc.reservation.dto.ReservationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepo;
    private final CourseClassRepository courseClassRepo;
    private final ResourceRepository resourceRepo;
    private final ScheduleSlotRepository scheduleSlotRepo;

    public ReservationService(
            ReservationRepository reservationRepo,
            CourseClassRepository courseClassRepo,
            ResourceRepository resourceRepo,
            ScheduleSlotRepository scheduleSlotRepo
    ) {
        this.reservationRepo = reservationRepo;
        this.courseClassRepo = courseClassRepo;
        this.resourceRepo = resourceRepo;
        this.scheduleSlotRepo = scheduleSlotRepo;
    }

    public List<ReservationResponseDTO> getAll() {
    return reservationRepo.findAll().stream()
            .map(res -> new ReservationResponseDTO(
                    res.getReservationId(),
                    res.getCourseClass().getName(),
                    res.getResource().getName(),
                    res.getReservationDate(),
                    res.getStartTime(),
                    res.getEndTime(),
                    res.getStatus()
            ))
            .toList();}

    public Reservation getById(Long id) {
        return reservationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation with ID " + id + " not found"));
    }

    public Reservation create(ReservationDTO dto) {
        CourseClass courseClass = courseClassRepo.findById(dto.getCourseClassId())
                .orElseThrow(() -> new NotFoundException("CourseClass not found"));
        Resource resource = resourceRepo.findById(dto.getResourceId())
                .orElseThrow(() -> new NotFoundException("Resource not found"));
        ScheduleSlot schedule = scheduleSlotRepo.findById(dto.getScheduleSlotId())
                .orElseThrow(() -> new NotFoundException("ScheduleSlot not found"));
         boolean exists = reservationRepo.existsByResourceAndReservationDateAndStartTime(
                resource, dto.getReservationDate(), dto.getStartTime()
        );
        if (exists)
            throw new BadRequestException("Resource already booked for this time.");

        Reservation r = new Reservation();
        r.setCourseClass(courseClass);
        r.setResource(resource);
        r.setScheduleSlot(schedule);
        r.setReservationDate(dto.getReservationDate());
        r.setStartTime(dto.getStartTime());
        r.setEndTime(dto.getEndTime());
        r.setStatus(ReservationStatus.PENDING);

        return reservationRepo.save(r);
    }

    public void delete(Long id) {
        if (!reservationRepo.existsById(id))
            throw new NotFoundException("Reservation not found.");
        reservationRepo.deleteById(id);
    }
}
