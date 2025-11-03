
package com.sarc.reservation.dto;

import com.sarc.domain.ReservationStatus;

/**
 * DTO simplificado para atualização apenas do status de uma reserva
 */
public class ReservationStatusUpdateDTO {
    private ReservationStatus status;

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
