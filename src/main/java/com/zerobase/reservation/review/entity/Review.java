package com.zerobase.reservation.review.entity;

import com.zerobase.reservation.global.entity.BaseEntity;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.reservation.entity.Reservation;
import com.zerobase.reservation.store.entity.Store;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private String content;
    private double rating;

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeRating(double rating) {
        this.rating = rating;
    }
}
