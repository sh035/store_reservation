package com.zerobase.reservation.manager.service;

import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import com.zerobase.reservation.manager.dto.ManagerDto;
import com.zerobase.reservation.manager.dto.ManagerSignUpDto;
import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.global.auth.enums.Role;
import com.zerobase.reservation.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param dto - 이메일, 이름, 비밀번호, 전화번호
     */
    @Transactional
    public ManagerDto signUp(ManagerSignUpDto.Request dto) {
        duplicateEmailCheck(dto.getEmail());

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        return ManagerDto.from(managerRepository.save(Manager.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .role(Role.ROLE_PARTNER)
                .build()));
    }

    /**
     * 이메일 중복체크 (회원가입)
     */
    public boolean duplicateEmailCheck(String email) {
        managerRepository.findByEmail(email).ifPresent(m -> {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        });
        return true;
    }
}
