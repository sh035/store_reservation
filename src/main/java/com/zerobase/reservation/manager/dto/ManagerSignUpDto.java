package com.zerobase.reservation.manager.dto;

import lombok.*;

public class ManagerSignUpDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String email;

        private String name;

        private String password;

        private String phone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private String name;

        public static Response from(ManagerDto dto) {
            return Response.builder()
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .build();
        }
    }
}
