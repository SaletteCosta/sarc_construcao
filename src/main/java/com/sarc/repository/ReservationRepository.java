package com.sarc.repository;

import com.sarc.domain.Reservation;
import com.sarc.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reservation r
        WHERE r.resource = :resource
          AND r.reservationDate = :date
          AND (
              (:startTime < r.endTime AND :endTime > r.startTime)
          )
    """)
    boolean existsOverlappingReservation(
            @Param("resource") Resource resource,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
