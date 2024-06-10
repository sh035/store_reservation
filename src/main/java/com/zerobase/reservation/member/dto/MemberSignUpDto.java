package com.zerobase.reservation.member.dto;

import lombok.*;


public class MemberSignUpDto {

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

        public static Response from(MemberDto dto) {
            return Response.builder()
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .build();
        }
    }
}
