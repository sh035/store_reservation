package com.zerobase.reservation.global.auth.service;

import com.zerobase.reservation.global.auth.dto.LoginDto;
import com.zerobase.reservation.global.auth.dto.TokenDto;
import com.zerobase.reservation.global.auth.dto.TokenRequestDto;
import com.zerobase.reservation.global.auth.entity.RefreshToken;
import com.zerobase.reservation.global.auth.jwt.TokenProvider;
import com.zerobase.reservation.global.auth.respository.RefreshTokenRepository;
import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     *  인증 객체를 바탕으로 Access Token, Refresh Token 생성
     *  Refresh 토큰은 저장하고 Access 토큰은 클라이언트에게 전달
     */
    public TokenDto login(LoginDto dto) {
        // Login email,password 로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = dto.toAuthentication();

        // 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    /**
     * Access Token 복호화하여 유저 정보 가져오고 DB에 있는 Refresh 토큰과
     * 클라이언트가 전달한 Refresh Token 일치 여부 검사하여
     * 일치하면 새로운 토큰 생성해서 클라이언트에게 전달
     */
    public TokenDto reissue(TokenRequestDto dto) {
        if (!tokenProvider.validationToken(dto.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Access Token 에서 User Id 가져옴
        Authentication authentication = tokenProvider.getAuthentication(dto.getAccessToken());

        // User ID 로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_USER));

        if (!refreshToken.getValue().equals(dto.getRefreshToken())) {
            throw new CustomException(ErrorCode.NOT_MATCH_USER);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(dto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }

}
