package com.zerobase.reservation.member.service;

import com.zerobase.reservation.global.auth.enums.Role;
import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import com.zerobase.reservation.member.dto.MemberDto;
import com.zerobase.reservation.member.dto.MemberSignUpDto;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param dto - 이메일, 이름, 비밀번호, 전화번호
     */
    @Transactional
    public MemberDto signUp(MemberSignUpDto.Request dto) {
        duplicateEmailCheck(dto.getEmail());

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        return MemberDto.from(memberRepository.save(Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .role(Role.ROLE_MEMBER)
                .build()));
    }

    /**
     * 이메일 중복체크 (회원가입)
     */
    public boolean duplicateEmailCheck(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        });
        return true;
    }
}
