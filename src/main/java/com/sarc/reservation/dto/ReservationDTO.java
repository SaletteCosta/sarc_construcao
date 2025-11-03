
package com.sarc.reservation.dto;

import com.sarc.domain.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO para transferência de dados de Reserva (Reservation)
 * Usado para criar e atualizar reservas através da API
 */
public class ReservationDTO {
    private Long classId;
    private Long resourceId;
    private Long scheduleSlotId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus status;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getScheduleSlotId() {
        return scheduleSlotId;
    }

    public void setScheduleSlotId(Long scheduleSlotId) {
        this.scheduleSlotId = scheduleSlotId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
