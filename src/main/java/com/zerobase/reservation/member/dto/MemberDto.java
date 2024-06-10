package com.zerobase.reservation.member.dto;

import com.zerobase.reservation.global.auth.enums.Role;
import com.zerobase.reservation.member.entity.Member;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private String email;
    private String name;
    private String password;
    private String phone;
    private Role role;

    public static MemberDto from(Member manager) {
        return MemberDto.builder()
                .email(manager.getEmail())
                .name(manager.getName())
                .password(manager.getPassword())
                .phone(manager.getPhone())
                .role(manager.getRole())
                .build();
    }
}
