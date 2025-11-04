package com.user.service;

import com.user.dto.ItemDTO;
import com.user.dto.ReservationDTO;
import com.user.entity.Item;
import com.user.entity.Reservation;
import com.user.entity.User;
import com.user.enums.ItemType;
import com.user.enums.ReservationStatus;
import com.user.repository.ItemRepository;
import com.user.repository.ReservationRepository;
import com.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    
    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        if (reservationRepository.existsByCode(reservationDTO.getCode())) {
            throw new RuntimeException("Reservation with code " + reservationDTO.getCode() + " already exists");
        }
        
        if (!userRepository.existsById(reservationDTO.getUserId())) {
            throw new RuntimeException("User not found with id: " + reservationDTO.getUserId());
        }
        
        if (!itemRepository.existsById(reservationDTO.getItemId())) {
            throw new RuntimeException("Item not found with id: " + reservationDTO.getItemId());
        }
        
        Reservation reservation = new Reservation();
        reservation.setCode(reservationDTO.getCode());
        reservation.setUserId(reservationDTO.getUserId());
        reservation.setItemId(reservationDTO.getItemId());
        reservation.setSchedule(reservationDTO.getSchedule());
        reservation.setReservationDate(LocalDate.parse(reservationDTO.getReservationDate()));
        reservation.setStatus(reservationDTO.getStatus() != null ? ReservationStatus.valueOf(reservationDTO.getStatus()) : ReservationStatus.ACTIVE);
        
        Reservation saved = reservationRepository.save(reservation);
        return toDTO(saved);
    }
    
    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public ReservationDTO createPeripheralReservation(ReservationDTO reservationDTO) {
        Item item = itemRepository.findById(reservationDTO.getItemId())
            .orElseThrow(() -> new RuntimeException("Item not found"));
        
        if (item.getType() != ItemType.PERIPHERAL) {
            throw new RuntimeException("Item is not a peripheral");
        }
        
        return createReservation(reservationDTO);
    }
    
    public ReservationDTO getReservationByCode(String code) {
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Reservation not found with code: " + code));
        return toDTO(reservation);
    }
    
    public List<ReservationDTO> getReservationsBySchedule(String schedule) {
        return reservationRepository.findBySchedule(schedule)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<ReservationDTO> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public String getReservationSchedule(String code) {
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Reservation not found with code: " + code));
        return reservation.getSchedule();
    }
    
    public List<ItemDTO> getItemsByType(String type) {
        ItemType itemType = ItemType.valueOf(type.toUpperCase());
        return itemRepository.findByType(itemType)
            .stream()
            .map(this::toItemDTO)
            .collect(Collectors.toList());
    }
    
    public List<ReservationDTO> getLaboratoriesByStudentRegistration(String registration) {
        return reservationRepository.findLaboratoriesByUserRegistration(registration)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    private ReservationDTO toDTO(Reservation reservation) {
        return new ReservationDTO(
            reservation.getId(),
            reservation.getCode(),
            reservation.getUserId(),
            reservation.getItemId(),
            reservation.getSchedule(),
            reservation.getReservationDate().toString(),
            reservation.getStatus().name()
        );
    }
    
    private ItemDTO toItemDTO(Item item) {
        return new ItemDTO(
            item.getId(),
            item.getCode(),
            item.getType().name(),
            item.getName(),
            item.getAvailable()
        );
    }
}
