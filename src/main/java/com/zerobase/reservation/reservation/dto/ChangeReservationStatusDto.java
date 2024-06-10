package com.zerobase.reservation.reservation.dto;

import com.zerobase.reservation.reservation.enums.ReservationStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeReservationStatusDto {
    private ReservationStatus reservationStatus;
}
