package com.zerobase.reservation.review.dto;

import lombok.*;

public class EditReviewDto {
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
        private String content;
        private double rating;

        public static Response from(ReviewDto dto) {
            return Response.builder()
                    .reviewId(dto.getId())
                    .content(dto.getContent())
                    .rating(dto.getRating())
                    .build();
        }
    }
}
