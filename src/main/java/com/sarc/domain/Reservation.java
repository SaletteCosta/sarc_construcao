package com.sarc.domain;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "reservation", indexes = {
        @Index(name = "idx_res_by_resource_date", columnList = "resource_id,reservation_date")
})
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private CourseClass courseClass;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleSlot scheduleSlot;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }
    public CourseClass getCourseClass() { return courseClass; }
    public void setCourseClass(CourseClass courseClass) { this.courseClass = courseClass; }
    public Resource getResource() { return resource; }
    public void setResource(Resource resource) { this.resource = resource; }
    public ScheduleSlot getScheduleSlot() { return scheduleSlot; }
    public void setScheduleSlot(ScheduleSlot scheduleSlot) { this.scheduleSlot = scheduleSlot; }
    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
}
