package com.user.repository;

import com.user.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByCode(String code);
    List<Reservation> findBySchedule(String schedule);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByItemId(Long itemId);
    List<Reservation> findByStatus(String status);
    
    @Query("SELECT r FROM Reservation r JOIN Item i ON r.itemId = i.id " +
           "JOIN User u ON r.userId = u.id " +
           "WHERE u.registration = :registration AND i.type = 'LABORATORY'")
    List<Reservation> findLaboratoriesByUserRegistration(@Param("registration") String registration);
    
    boolean existsByCode(String code);
}
