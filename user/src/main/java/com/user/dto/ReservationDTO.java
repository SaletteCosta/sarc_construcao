package com.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private Long id;
    private String code;
    private Long userId;
    private Long itemId;
    private String schedule;
    private String reservationDate;
    private String status;
}
