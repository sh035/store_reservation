package com.zerobase.reservation.global.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ROLE_MEMBER("일반 사용자"), ROLE_PARTNER("파트너");
    private String value;

}
