package com.zerobase.reservation.reservation.repository;

import com.zerobase.reservation.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByReservationTime(@Param("reservationTime") LocalDateTime reservationTime);

    @Query("SELECT r FROM Reservation r JOIN r.store s WHERE s.manager.id = :managerId ORDER BY r.reservationTime DESC")
    List<Reservation> findAllByManagerIdOrderByReservationTimeDesc(@Param("managerId") Long managerId);

    @Query("SELECT r FROM Reservation r WHERE r.member.id = :memberId ORDER BY r.reservationTime DESC")
    List<Reservation> findAllByMemberIdOrderByReservationTimeDesc(@Param("memberId") Long memberId);
}
