package com.zerobase.reservation.global.dto;

import com.zerobase.reservation.global.exception.ErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private ErrorCode errorCode;
    private String ErrorMessage;
}
