package com.zerobase.reservation.reservation.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ArrivalStatus {

    NOT_ARRIVING("미도착"),
    ARRIVED("도착");

    private final String description;
}
