package com.zerobase.reservation.reservation.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateReservationDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long storeId;
        private Long memberId;

        private Integer people;
        private LocalDate reservationDate;
        private LocalTime reservationTime;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long storeId;
        private Long memberId;
        private Integer people;
        private LocalDateTime reservationTime;

        public static Response from(ReservationDto dto) {
            return Response.builder()
                    .storeId(dto.getStoreId())
                    .memberId(dto.getMemberId())
                    .people(dto.getPeople())
                    .reservationTime(dto.getReservationTime())
                    .build();
        }
    }
}
