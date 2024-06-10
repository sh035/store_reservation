package com.zerobase.reservation.global.auth.service;

import com.zerobase.reservation.global.auth.enums.Role;
import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.manager.repository.ManagerRepository;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    /**
     * 유저 이메일을 통해 DB에 있는 계정을 찾음
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        if (memberRepository.existsByEmail(username)) {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
            return createUserDetails(member.getEmail(), member.getPassword(), member.getRole());
        }
        if (managerRepository.existsByEmail(username)) {
            Manager manager = managerRepository.findByEmail(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MANAGER));
            return createUserDetails(manager.getEmail(), manager.getPassword(), manager.getRole());
        }

        throw new UsernameNotFoundException("유저 정보를 찾을 수 없습니다.");
    }

    /**
     * DB에 유저 값이 존재하면 UserDetails 객체로 만들어 리턴
     */
    private UserDetails createUserDetails(String username, String password, Role role) {
        return User.withUsername(username)
                .password(password)
                .authorities(role.name())
                .build();
    }
}
