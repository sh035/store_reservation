package com.zerobase.reservation.review.service;

import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.member.repository.MemberRepository;
import com.zerobase.reservation.reservation.entity.Reservation;
import com.zerobase.reservation.reservation.enums.ReservationStatus;
import com.zerobase.reservation.reservation.repository.ReservationRepository;
import com.zerobase.reservation.review.dto.CreateReviewDto;
import com.zerobase.reservation.review.dto.EditReviewDto;
import com.zerobase.reservation.review.dto.ReviewDto;
import com.zerobase.reservation.review.entity.Review;
import com.zerobase.reservation.review.repository.ReviewRepository;
import com.zerobase.reservation.store.entity.Store;
import com.zerobase.reservation.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성
     * @param memberId - 로그인 인덱스 (사용자)
     * @param storeId - 매장 인덱스
     * @param reservationId - 예약 인덱스
     * @param dto - 리뷰 내용, 평점
     */
    @Transactional
    public ReviewDto create(Long memberId, Long storeId,
                            Long reservationId, CreateReviewDto.Request dto) {
        Member member = getMember(memberId);
        Store store = getStore(storeId);
        Reservation reservation = getReservation(reservationId);

        validateCreateReview(member, reservation);
        validateReviewDetail(dto.getContent(), dto.getRating());

        return ReviewDto.from(reviewRepository.save(Review.builder()
                .store(store)
                .member(member)
                .reservation(reservation)
                .content(dto.getContent())
                .rating(dto.getRating())
                .build()));
    }

    /**
     * 리뷰 수정
     * @param memberId - 로그인 인덱스 (사용자)
     * @param reviewId - 리뷰 인덱스
     * @param dto - 리뷰 내용, 평점
     */
    @Transactional
    public ReviewDto edit(Long memberId, Long reviewId, EditReviewDto.Request dto) {
        Review review = getReview(reviewId);

        if (review.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
        }

        validateReviewDetail(dto.getContent(), dto.getRating());

        review.changeContent(dto.getContent());
        review.changeRating(dto.getRating());

        return ReviewDto.from(reviewRepository.save(review));
    }

    /**
     * 리뷰 삭제
     * @param email - 로그인 이메일 (사용자, 매니저)
     * @param reviewId - 리뷰 인덱스
     */
    @Transactional
    public void delete(String email, Long reviewId) {
        Review review = getReview(reviewId);
        validateAuthority(email, review);

        reviewRepository.delete(review);
    }

    /**
     * 사용자 정보 가져오기
     */
    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    /**
     * 매니저 정보 가져오기
     */
    private Store getStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));
    }

    /**
     * 예약 정보 가져오기
     */
    private Reservation getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));
    }

    /**
     * 리뷰 정보 가져오기
     */
    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
    }

    /**
     * 리뷰 작성 관련 검증
     * 1. 이미 리뷰가 작성된 경우
     * 2. 예약한 사용자와 리뷰 작성을 할 사용자가 다른 경우
     * 3. 예약 상태가 완료가 아닌 경우
     */
    private void validateCreateReview(Member member, Reservation reservation) {
        if (reviewRepository.existsByReservationId(reservation.getId())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
        }
        if (!reservation.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_MEMBER);
        }
        if (!reservation.getReservationStatus().equals(ReservationStatus.COMPLETION)) {
            throw new CustomException(ErrorCode.NOT_COMPLETED_RESERVATION);
        }
    }

    /**
     * 리뷰 내용 관련 검증
     * 1. 작성 글은 200자 이하
     * 2. 평점은 0점 이상 5점 이하여야 한다.
     */
    private void validateReviewDetail(String content, double rating) {
        if (content.length() > 200) {
            throw new CustomException(ErrorCode.TOO_MANY_CHARACTERS);
        }
        if (rating < 0 || rating > 5) {
            throw new CustomException(ErrorCode.EXCEEDED_RATING_RANGE);
        }
    }

    /**
     * 리뷰를 작성한 사용자 또는 리뷰가 작성된 매장의 직원인지 검증
     */
    private void validateAuthority(String email, Review review) {
        if (!(review.getMember().getEmail() == email ||
                review.getStore().getManager().getEmail() == email)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
    }
}
