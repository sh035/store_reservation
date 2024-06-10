package com.zerobase.reservation.review.controller;

import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.review.dto.CreateReviewDto;
import com.zerobase.reservation.review.dto.EditReviewDto;
import com.zerobase.reservation.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 작성
     * @param member - 로그인 정보 (사용자)
     * @param storeId - 매장 인덱스
     * @param reservationId - 예약 인덱스
     * @param dto - 리뷰 내용, 평점
     */
    @PostMapping("/member/create/{reservationId}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> createReview(@AuthenticationPrincipal Member member,
                                          @RequestParam(name = "storeId") Long storeId,
                                          @RequestParam(name = "reservationId") Long reservationId,
                                          @RequestBody CreateReviewDto.Request dto) {
        return ResponseEntity.ok().body(CreateReviewDto.Response.from(
                reviewService.create(member.getId(), storeId, reservationId, dto)));
    }

    /**
     * 리뷰 수정
     * @param member - 로그인 정보 (사용자)
     * @param reviewId - 리뷰 인덱스
     * @param dto - 리뷰 내용, 평점
     */
    @PutMapping("/member/edit/{reviewId}")
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<?> editReview(@AuthenticationPrincipal Member member,
                                        @PathVariable Long reviewId,
                                        @RequestBody EditReviewDto.Request dto) {
        return ResponseEntity.ok().body(EditReviewDto.Response.from(
                reviewService.edit(member.getId(), reviewId, dto)
        ));
    }

    /**
     * 리뷰 삭제
     * @param userDetails - 로그인 정보 (사용자, 매니저)
     * @param reviewId - 리뷰 인덱스
     * @return
     */
    @DeleteMapping("/delete/{reviewId}")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER', 'ROLE_PARTNER')")
    public ResponseEntity<?> deleteReview(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable Long reviewId) {
        reviewService.delete(userDetails.getUsername(), reviewId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

}
