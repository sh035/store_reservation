package com.zerobase.reservation.reservation.service;

import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.manager.repository.ManagerRepository;
import com.zerobase.reservation.member.entity.Member;
import com.zerobase.reservation.member.repository.MemberRepository;
import com.zerobase.reservation.reservation.dto.ChangeReservationStatusDto;
import com.zerobase.reservation.reservation.dto.CreateReservationDto;
import com.zerobase.reservation.reservation.dto.ReservationDto;
import com.zerobase.reservation.reservation.entity.Reservation;
import com.zerobase.reservation.reservation.enums.ReservationStatus;
import com.zerobase.reservation.reservation.enums.ArrivalStatus;
import com.zerobase.reservation.reservation.repository.ReservationRepository;
import com.zerobase.reservation.store.entity.Store;
import com.zerobase.reservation.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final ManagerRepository managerRepository;

    /**
     * 유저 - 매장 예약
     * @param memberId - 유저의 계정
     * @param dto - 매장 인덱스, 사용자 인덱스, 인원 수, 예약 날짜, 예약 시간
     */
    @Transactional
    public ReservationDto create(Long memberId, CreateReservationDto.Request dto) {

        Member member = getMember(memberId);
        Store store = getStore(dto.getStoreId());
        LocalDateTime reservationTime = LocalDateTime.of(
                dto.getReservationDate(), dto.getReservationTime());

        reservationTimeCheck(reservationTime);

        return ReservationDto.from(reservationRepository.save(Reservation.builder()
                .store(store)
                .member(member)
                .people(dto.getPeople())
                .reservationStatus(ReservationStatus.REQUEST)
                .arrivalStatus(ArrivalStatus.NOT_ARRIVING)
                .reservationTime(reservationTime)
                .build()));
    }

    /**
     * 유저, 매니저 - 예약에 대한 상세내역 확인
     */
    public ReservationDto detail(String email, Long reservationId) {
        Reservation reservation = getReservation(reservationId);
        validateAuthority(email, reservation);
        return ReservationDto.from(reservation);
    }

    /**
     * 매니저 - 매장 예약 목록 내역 확인 (최신순)
     */
    public List<ReservationDto> listByManager(Long managerId) {
        Manager manager = getManager(managerId);
        List<Reservation> list
                = reservationRepository.findAllByManagerIdOrderByReservationTimeDesc(manager.getId());

        validateReservationList(list);

        return list.stream()
                .map(ReservationDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 유저 - 예약 목록 내역 확인 (최신순)
     */
    public List<ReservationDto> listByMember(Long memberId) {
        List<Reservation> list
                = reservationRepository.findAllByManagerIdOrderByReservationTimeDesc(memberId);
        
        validateReservationList(list);

        return list.stream()
                .map(ReservationDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 매니저 - 예약 상태 변경
     */
    @Transactional
    public ReservationDto changeReservationStatus(Long managerId, Long reservationId, ChangeReservationStatusDto dto) {
        Reservation reservation = getReservation(reservationId);
        Manager manager = getManager(managerId);
        validateMatchStoreToManager(manager.getId(), reservation);

        reservation.changeReservationStatus(dto.getReservationStatus());

        return ReservationDto.from(reservationRepository.save(reservation));
    }



    /**
     * 도착 상태 변경
     */
    @Transactional
    public ReservationDto changeArrivalStatus(Long reservationId) {
        Reservation reservation = getReservation(reservationId);

        validateArrival(reservation, LocalDateTime.now());

        reservation.changeArrivalStatus(ArrivalStatus.ARRIVED);
        reservation.changeReservationStatus(ReservationStatus.COMPLETION);

        return ReservationDto.from(reservationRepository.save(reservation));
    }

    /**
     * 유저, 매니저 - 예약에 대한 권한체크
     */
    private void validateAuthority(String email, Reservation reservation) {
        if (!(reservation.getMember().getEmail() == email ||
                reservation.getStore().getManager().getEmail() == email)) {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
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
    private Manager getManager(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MANAGER));
    }

    /**
     * 매장 정보 가져오기
     */
    private Store getStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));
    }

    /**
     * 예약 정보 가져오기
     */
    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_RESERVATION));
    }

    /**
     *  예약하려고 하는 시간에 예약 있는지 확인
     */
    private void reservationTimeCheck(LocalDateTime reservationTime) {
        if (reservationRepository.existsByReservationTime(reservationTime)) {
            throw new CustomException(ErrorCode.ALREADY_RESERVED_TIME);
        }
    }

    /**
     * 매장의 매니저와 매장 정보를 수정하려는 매니저가 일치하는지 검증
     */
    private void validateMatchStoreToManager(Long managerId, Reservation reservation) {
        if (!reservation.getStore().getManager().getId().equals(managerId)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_STORE_TO_MANAGER);
        }
    }

    /**
     *  매장에 예약이 비어있는지 검증
     */
    private void validateReservationList(List<Reservation> list) {
        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_RESERVATION_LIST);
        }
    }

    /**
     * 예약 유효성 검사
     * 1. 예약 상태가 승인인지 확인
     * 2. 예약 10분 전에 도착했는지 확인
     */
    private void validateArrival(Reservation reservation, LocalDateTime arrivalTime) {
        
        if (!reservation.getReservationStatus().equals(ReservationStatus.APPROVAL)) {
            throw new CustomException(ErrorCode.REJECT_RESERVATION_STATUS);
        }
        if (arrivalTime.isAfter(reservation.getReservationTime().minusMinutes(10L))) {
            throw new CustomException(ErrorCode.TIME_OUT_RESERVATION_TIME);
        }
    }


}
