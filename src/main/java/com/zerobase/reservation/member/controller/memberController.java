package com.zerobase.reservation.member.controller;

import com.zerobase.reservation.member.dto.MemberDto;
import com.zerobase.reservation.member.dto.MemberSignUpDto;
import com.zerobase.reservation.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class memberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * @param dto - 이메일, 이름, 비밀번호, 전화번호
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@RequestBody MemberSignUpDto.Request dto) {
        MemberDto member = memberService.signUp(dto);
        return ResponseEntity.ok().body(MemberSignUpDto.Response.from(member));
    }
}
