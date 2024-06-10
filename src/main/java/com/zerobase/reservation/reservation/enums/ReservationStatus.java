package com.zerobase.reservation.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    REQUEST("요청"),
    APPROVAL("승인"),
    REJECTION("거절"),
    CANCEL("취소"),
    COMPLETION("완료");


    private final String description;
}
