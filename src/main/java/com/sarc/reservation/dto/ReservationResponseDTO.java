package com.sarc.reservation.dto;

import com.sarc.domain.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationResponseDTO {
    private Long id;
    private String courseClassName;
    private String resourceName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus status;

    public ReservationResponseDTO(Long id, String courseClassName, String resourceName,
                                  LocalDate reservationDate, LocalTime startTime,
                                  LocalTime endTime, ReservationStatus status) {
        this.id = id;
        this.courseClassName = courseClassName;
        this.resourceName = resourceName;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() { return id; }
    public String getCourseClassName() { return courseClassName; }
    public String getResourceName() { return resourceName; }
    public LocalDate getReservationDate() { return reservationDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public ReservationStatus getStatus() { return status; }
}
