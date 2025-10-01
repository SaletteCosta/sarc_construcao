package com.sarc.repository;

import com.sarc.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByResource_ResourceIdAndReservationDate(Long resourceId, LocalDate date);
}
