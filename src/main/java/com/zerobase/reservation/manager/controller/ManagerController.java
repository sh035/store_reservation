package com.zerobase.reservation.manager.controller;

import com.zerobase.reservation.manager.dto.ManagerDto;
import com.zerobase.reservation.manager.dto.ManagerSignUpDto;
import com.zerobase.reservation.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    /**
     * 회원가입
     * @param dto - 이메일, 이름, 비밀번호, 전화번호
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUpManager(@RequestBody ManagerSignUpDto.Request dto) {

        ManagerDto manager = managerService.signUp(dto);
        return ResponseEntity.ok().body(ManagerSignUpDto.Response.from(manager));
    }
}
