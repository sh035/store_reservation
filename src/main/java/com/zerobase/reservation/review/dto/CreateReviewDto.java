package com.zerobase.reservation.review.dto;

import lombok.*;

public class CreateReviewDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String content;
        private double rating;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long reviewId;
        private Long memberId;
        private Long storeId;
        private String content;
        private double rating;

        public static Response from(ReviewDto dto) {
            return Response.builder()
                    .reviewId(dto.getId())
                    .memberId(dto.getMemberId())
                    .storeId(dto.getStoreId())
                    .content(dto.getContent())
                    .rating(dto.getRating())
                    .build();
        }
    }
}
