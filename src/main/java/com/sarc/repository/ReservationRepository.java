package com.sarc.repository;

import com.sarc.domain.Reservation;
import com.sarc.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByResource_ResourceIdAndReservationDate(Long resourceId, LocalDate date);
    List<Reservation> findByCourseClass_ClassId(Long classId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByResource_ResourceId(Long resourceId);
}
