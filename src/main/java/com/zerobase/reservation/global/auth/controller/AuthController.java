package com.zerobase.reservation.global.auth.controller;

import com.zerobase.reservation.global.auth.dto.LoginDto;
import com.zerobase.reservation.global.auth.dto.TokenDto;
import com.zerobase.reservation.global.auth.dto.TokenRequestDto;
import com.zerobase.reservation.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 - jwt 토큰 반환
     * @param dto - 이메일, 비밀번호
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto) {
        return ResponseEntity.ok().body(authService.login(dto));
    }

    /**
     * refresh 토큰 발급
     * @param dto - 사용자 아이디, refresh 토큰
     */
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto dto) {
        return ResponseEntity.ok().body(authService.reissue(dto));
    }
}
