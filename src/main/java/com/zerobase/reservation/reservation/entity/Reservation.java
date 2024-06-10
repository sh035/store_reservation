package com.zerobase.reservation.reservation.entity;

import com.zerobase.reservation.global.entity.BaseEntity;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.reservation.enums.ReservationStatus;
import com.zerobase.reservation.reservation.enums.ArrivalStatus;
import com.zerobase.reservation.store.entity.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Integer people;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Enumerated(EnumType.STRING)
    private ArrivalStatus arrivalStatus;

    private LocalDateTime reservationTime;

    public void changeReservationStatus(ReservationStatus status) {
        this.reservationStatus = status;
    }

    public void changeArrivalStatus(ArrivalStatus status) {
        this.arrivalStatus = status;
    }
}
