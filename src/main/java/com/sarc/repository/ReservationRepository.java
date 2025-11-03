package com.sarc.repository;

import com.sarc.domain.Reservation;
import com.sarc.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByResourceAndReservationDateAndStartTime(
            Resource resource,
            LocalDate reservationDate,
            LocalTime startTime
    );
}
