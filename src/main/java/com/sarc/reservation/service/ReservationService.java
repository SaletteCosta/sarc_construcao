package com.sarc.reservation.service;

import com.sarc.domain.*;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.*;
import com.sarc.reservation.dto.ReservationDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de Reservas (Reservation)
 * Implementa toda a lógica de negócio relacionada a reservas,
 * incluindo validações críticas de disponibilidade e conflitos
 */
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CourseClassRepository courseClassRepository;
    private final ResourceRepository resourceRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            CourseClassRepository courseClassRepository,
            ResourceRepository resourceRepository,
            ScheduleSlotRepository scheduleSlotRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.courseClassRepository = courseClassRepository;
        this.resourceRepository = resourceRepository;
        this.scheduleSlotRepository = scheduleSlotRepository;
    }

    /**
     * Lista todas as reservas
     * @return Lista de todas as reservas
     */
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    /**
     * Busca uma reserva por ID
     * @param id ID da reserva
     * @return Reserva encontrada
     * @throws NotFoundException se a reserva não existir
     */
    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation with ID " + id + " not found"));
    }

    /**
     * Busca reservas por recurso e data
     * @param resourceId ID do recurso
     * @param date Data da reserva
     * @return Lista de reservas do recurso na data especificada
     */
    public List<Reservation> getByResourceAndDate(Long resourceId, LocalDate date) {
        return reservationRepository.findByResource_ResourceIdAndReservationDate(resourceId, date);
    }

    /**
     * Busca reservas por turma
     * @param classId ID da turma
     * @return Lista de reservas da turma
     */
    public List<Reservation> getByClassId(Long classId) {
        return reservationRepository.findByCourseClass_ClassId(classId);
    }

    /**
     * Busca reservas por status
     * @param status Status da reserva
     * @return Lista de reservas com o status especificado
     */
    public List<Reservation> getByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    /**
     * Busca reservas por recurso
     * @param resourceId ID do recurso
     * @return Lista de reservas do recurso
     */
    public List<Reservation> getByResourceId(Long resourceId) {
        return reservationRepository.findByResource_ResourceId(resourceId);
    }

    /**
     * Cria uma nova reserva com validações completas
     * @param dto Dados da reserva
     * @return Reserva criada
     * @throws BadRequestException se os dados forem inválidos ou houver conflito
     * @throws NotFoundException se turma, recurso ou slot não existirem
     */
    public Reservation create(ReservationDTO dto) {
        // ========== VALIDAÇÕES BÁSICAS ==========
        validateReservationDTO(dto);

        // ========== BUSCA ENTIDADES ==========
        CourseClass courseClass = courseClassRepository.findById(dto.getClassId())
                .orElseThrow(() -> new NotFoundException("CourseClass with ID " + dto.getClassId() + " not found"));

        Resource resource = resourceRepository.findById(dto.getResourceId())
                .orElseThrow(() -> new NotFoundException("Resource with ID " + dto.getResourceId() + " not found"));

        ScheduleSlot scheduleSlot = scheduleSlotRepository.findById(dto.getScheduleSlotId())
                .orElseThrow(() -> new NotFoundException("ScheduleSlot with ID " + dto.getScheduleSlotId() + " not found"));

        // ========== VALIDAÇÕES DE NEGÓCIO ==========
        
        // 1. Validar se o slot de horário pertence ao recurso
        if (!scheduleSlot.getResource().getResourceId().equals(dto.getResourceId())) {
            throw new BadRequestException(
                "ScheduleSlot with ID " + dto.getScheduleSlotId() + 
                " does not belong to Resource with ID " + dto.getResourceId()
            );
        }

        // 2. Validar se o dia da semana da reserva corresponde ao slot
        int dayOfWeek = dto.getReservationDate().getDayOfWeek().getValue() % 7; // Converte para 0-6 (0=Domingo)
        if (scheduleSlot.getDayOfWeek() != dayOfWeek) {
            throw new BadRequestException(
                "Reservation date " + dto.getReservationDate() + 
                " does not match ScheduleSlot day of week (expected: " + scheduleSlot.getDayOfWeek() + ")"
            );
        }

        // 3. Validar se o horário da reserva está dentro do slot
        if (dto.getStartTime().isBefore(scheduleSlot.getStartTime()) || 
            dto.getEndTime().isAfter(scheduleSlot.getEndTime())) {
            throw new BadRequestException(
                "Reservation time (" + dto.getStartTime() + " - " + dto.getEndTime() + 
                ") is outside the ScheduleSlot time range (" + 
                scheduleSlot.getStartTime() + " - " + scheduleSlot.getEndTime() + ")"
            );
        }

        // 4. Validar se start_time < end_time
        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        // 5. Validar se não há conflito com outras reservas confirmadas
        validateNoConflict(dto.getResourceId(), dto.getReservationDate(), dto.getStartTime(), dto.getEndTime(), null);

        // ========== CRIAÇÃO DA RESERVA ==========
        Reservation reservation = new Reservation();
        reservation.setCourseClass(courseClass);
        reservation.setResource(resource);
        reservation.setScheduleSlot(scheduleSlot);
        reservation.setReservationDate(dto.getReservationDate());
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setStatus(dto.getStatus() != null ? dto.getStatus() : ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }

    /**
     * Atualiza o status de uma reserva
     * @param id ID da reserva
     * @param newStatus Novo status
     * @return Reserva atualizada
     * @throws NotFoundException se a reserva não existir
     */
    public Reservation updateStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation with ID " + id + " not found"));

        if (newStatus == null) {
            throw new BadRequestException("Status cannot be null");
        }

        reservation.setStatus(newStatus);
        return reservationRepository.save(reservation);
    }

    /**
     * Cancela uma reserva (muda status para DENIED)
     * @param id ID da reserva
     * @return Reserva cancelada
     */
    public Reservation cancel(Long id) {
        return updateStatus(id, ReservationStatus.DENIED);
    }

    /**
     * Deleta uma reserva
     * @param id ID da reserva
     * @throws NotFoundException se a reserva não existir
     */
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException("Reservation with ID " + id + " not found");
        }
        reservationRepository.deleteById(id);
    }

    // ========== MÉTODOS AUXILIARES DE VALIDAÇÃO ==========

    /**
     * Valida os dados básicos do DTO
     */
    private void validateReservationDTO(ReservationDTO dto) {
        if (dto.getClassId() == null) {
            throw new BadRequestException("Class ID cannot be null");
        }
        if (dto.getResourceId() == null) {
            throw new BadRequestException("Resource ID cannot be null");
        }
        if (dto.getScheduleSlotId() == null) {
            throw new BadRequestException("ScheduleSlot ID cannot be null");
        }
        if (dto.getReservationDate() == null) {
            throw new BadRequestException("Reservation date cannot be null");
        }
        if (dto.getStartTime() == null) {
            throw new BadRequestException("Start time cannot be null");
        }
        if (dto.getEndTime() == null) {
            throw new BadRequestException("End time cannot be null");
        }
    }

    /**
     * Valida se não há conflito de horário com outras reservas confirmadas
     * @param resourceId ID do recurso
     * @param date Data da reserva
     * @param startTime Hora de início
     * @param endTime Hora de fim
     * @param excludeReservationId ID da reserva a excluir da validação (para updates)
     * @throws BadRequestException se houver conflito
     */
    private void validateNoConflict(
            Long resourceId, 
            LocalDate date, 
            LocalTime startTime, 
            LocalTime endTime,
            Long excludeReservationId
    ) {
        List<Reservation> existingReservations = reservationRepository
                .findByResource_ResourceIdAndReservationDate(resourceId, date);

        // Filtra apenas reservas confirmadas ou pendentes (não canceladas/concluídas)
        List<Reservation> activeReservations = existingReservations.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED || 
                            r.getStatus() == ReservationStatus.PENDING)
                .filter(r -> excludeReservationId == null || !r.getReservationId().equals(excludeReservationId))
                .collect(Collectors.toList());

        // Verifica sobreposição de horários
        for (Reservation existing : activeReservations) {
            boolean hasOverlap = !(endTime.isBefore(existing.getStartTime()) || 
                                  endTime.equals(existing.getStartTime()) ||
                                  startTime.isAfter(existing.getEndTime()) ||
                                  startTime.equals(existing.getEndTime()));

            if (hasOverlap) {
                throw new BadRequestException(
                    "Time conflict: Resource is already reserved from " + 
                    existing.getStartTime() + " to " + existing.getEndTime() +
                    " (Reservation ID: " + existing.getReservationId() + ")"
                );
            }
        }
    }
}
