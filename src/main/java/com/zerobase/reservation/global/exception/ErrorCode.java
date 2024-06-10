package com.zerobase.reservation.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ALREADY_EXIST_USER("이미 존재하는 회원입니다."),
    ALREADY_REGISTERED_MANAGER("이미 매장을 등록한 매니저입니다."),
    ALREADY_REGISTERED_STORE("이미 등록된 매장입니다."),
    ALREADY_RESERVED_TIME("이미 예약된 시간입니다."),
    ALREADY_EXIST_REVIEW("이미 존재하는 리뷰입니다."),

    NOT_FOUND_MEMBER("등록되지 않은 회원입니다."),
    NOT_FOUND_MANAGER("등록되지 않은 매니저입니다."),
    NOT_FOUND_STORE("등록되지 않은 매장입니다."),
    NOT_FOUND_RESERVATION("등록되지 않은 예약입니다."),
    NOT_FOUND_REVIEW("등록되지 않은 리뷰입니다."),
    NOT_MATCHED_STORE_TO_MANAGER("매니저님의 매장이 아닙니다."),
    NOT_FOUND_RESERVATION_LIST("등록된 예약이 존재하지 않습니다."),
    NOT_AUTHORIZED("권한이 없습니다."),
    NOT_MATCH_MEMBER("해당 예약자와 사용자가 일치하지 않습니다."),
    NOT_COMPLETED_RESERVATION("예약이 완료상태가 아닙니다."),

    TOO_MANY_CHARACTERS("글자 수는 200자 제한입니다."),
    EXCEEDED_RATING_RANGE("평점 범위를 초과 했습니다. 0 <= 평점 <= 5"),

    // reservation
    REJECT_RESERVATION_STATUS("거절된 예약입니다."),
    TIME_OUT_RESERVATION_TIME("10분 전에 도착하지 못하였습니다."),

    // jwt
    ACCESS_DENIED("접근 권한이 없습니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN("유효하지 않은 RefreshToken 입니다."),
    LOGOUT_USER("로그아웃 된 사용자입니다."),
    NOT_MATCH_USER("토큰의 유저 정보가 일치하지 않습니다."),

    // global
    NO_HAVE_AUTHENTICATION_INFORMATION("Security Context에 인증 정보가 없습니다.")
    ;

    private final String description;
}
