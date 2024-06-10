package com.zerobase.reservation.reservation.dto;

import com.zerobase.reservation.reservation.entity.Reservation;
import com.zerobase.reservation.reservation.enums.ReservationStatus;
import com.zerobase.reservation.reservation.enums.ArrivalStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDto {

    private Long storeId;
    private Long memberId;

    private Integer people;
    private ReservationStatus reservationStatus;
    private ArrivalStatus arrivalStatus;
    private LocalDateTime reservationTime;

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
                .storeId(reservation.getStore().getId())
                .memberId(reservation.getMember().getId())
                .people(reservation.getPeople())
                .reservationStatus(reservation.getReservationStatus())
                .arrivalStatus(reservation.getArrivalStatus())
                .reservationTime(reservation.getReservationTime())
                .build();
    }
}
