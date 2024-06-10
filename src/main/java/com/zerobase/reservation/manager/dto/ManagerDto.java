package com.zerobase.reservation.manager.dto;

import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.global.auth.enums.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerDto {

    private String email;
    private String name;
    private String password;
    private String phone;
    private Role role;

    public static ManagerDto from(Manager manager) {
        return ManagerDto.builder()
                .email(manager.getEmail())
                .name(manager.getName())
                .password(manager.getPassword())
                .phone(manager.getPhone())
                .role(manager.getRole())
                .build();
    }

}
