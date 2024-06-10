package com.zerobase.reservation.reservation.controller;

import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.reservation.dto.ChangeArrivalStatusDto;
import com.zerobase.reservation.reservation.dto.ChangeReservationStatusDto;
import com.zerobase.reservation.reservation.dto.CreateReservationDto;
import com.zerobase.reservation.reservation.dto.ReservationDto;
import com.zerobase.reservation.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * 매장 예약 상세정보
     * @param userDetails - 로그인 정보 (사용자, 매니저)
     * @param reservationId - 예약 인덱스
     */
    @GetMapping("/detail/{reservationId}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_PARTNER')")
    public ResponseEntity<?> detailReservation(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.detail(userDetails.getUsername(), reservationId));
    }

    /**
     * 도착 상태 변경
     * @param reservationId - 예약 인덱스
     * @return
     */
    @PostMapping("/arrival/{reservationId}")
    public ResponseEntity<?> changeArrivalStatus(@PathVariable Long reservationId) {
        return ResponseEntity.ok(
                ChangeArrivalStatusDto.from(reservationService.changeArrivalStatus(reservationId)));
    }

    /**
     * 매장 예약
     * @param member - 로그인 정보 (사용자)
     * @param dto - 매장 인덱스, 사용자 인덱스, 인원 수, 예약 날짜, 예약 시간
     * @return
     */
    @PostMapping("/member/create")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> createReservation(@AuthenticationPrincipal Member member,
                                               @RequestBody CreateReservationDto.Request dto) {
        return ResponseEntity.ok().body(
                CreateReservationDto.Response.from(reservationService.create(member.getId(), dto)));
    }

    /**
     * 사용자가 예약한 매장 리스트
     * @param member - 로그인 정보 (사용자)
     */
    @GetMapping("/member/list")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<List<ReservationDto>> ReservationListByMember(
            @AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(reservationService.listByMember(member.getId()));
    }

    /**
     * 매니저가 확인하는 매장 예약 리스트
     * @param manager - 로그인 정보 (매니저)
     * @return
     */
    @GetMapping("/manager/list")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<List<ReservationDto>> ReservationListByManager(
            @AuthenticationPrincipal Manager manager
    ) {
        return ResponseEntity.ok().body(reservationService.listByManager(manager.getId()));
    }

    /**
     * 예약 변경
     * @param manager - 로그인 정보 (매니저)
     * @param reservationId - 매장 인덱스
     * @param dto - 예약 상태
     */
    @PutMapping("/manager/status/{reservationId}")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<ReservationDto> changeReservationStatus(
            @AuthenticationPrincipal Manager manager,
            @PathVariable Long reservationId,
            @RequestBody ChangeReservationStatusDto dto) {

        return ResponseEntity.ok(
                reservationService.changeReservationStatus(manager.getId(), reservationId, dto));
    }
}
