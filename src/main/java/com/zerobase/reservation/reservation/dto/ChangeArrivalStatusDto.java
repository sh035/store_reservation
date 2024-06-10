package com.zerobase.reservation.reservation.dto;

import com.zerobase.reservation.reservation.enums.ArrivalStatus;
import com.zerobase.reservation.reservation.enums.ReservationStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeArrivalStatusDto {

    private Long storeId;
    private Long memberId;
    private Integer people;
    private ReservationStatus reservationStatus;
    private ArrivalStatus arrivalStatus;

    public static ChangeArrivalStatusDto from(ReservationDto dto) {
        return ChangeArrivalStatusDto.builder()
                .storeId(dto.getStoreId())
                .memberId(dto.getMemberId())
                .people(dto.getPeople())
                .reservationStatus(dto.getReservationStatus())
                .arrivalStatus(dto.getArrivalStatus())
                .build();
    }
}
