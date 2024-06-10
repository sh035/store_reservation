package com.zerobase.reservation.review.dto;

import com.zerobase.reservation.review.entity.Review;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Long storeId;
    private Long memberId;
    private Long reservationId;
    private String content;
    private double rating;

    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .storeId(review.getStore().getId())
                .memberId(review.getMember().getId())
                .reservationId(review.getReservation().getId())
                .content(review.getContent())
                .rating(review.getRating())
                .build();
    }
}
