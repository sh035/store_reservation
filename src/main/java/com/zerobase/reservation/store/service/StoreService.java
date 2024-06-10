package com.zerobase.reservation.store.service;

import com.zerobase.reservation.global.exception.CustomException;
import com.zerobase.reservation.global.exception.ErrorCode;
import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.manager.repository.ManagerRepository;
import com.zerobase.reservation.store.dto.StoreDto;
import com.zerobase.reservation.store.entity.Store;
import com.zerobase.reservation.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final ManagerRepository managerRepository;

    /**
     * 매장 등록
     * @param managerId - 매니저 인덱스
     * @param dto - 매니저 인덱스, 매장 이름, 설명, 위치
     */
    @Transactional
    public StoreDto register(Long managerId, StoreDto dto) {
        Manager manager = getManager(managerId);

        validateRegister(manager.getId(), dto.getStoreName());

        return StoreDto.from(storeRepository.save(Store.builder()
                .manager(manager)
                .storeName(dto.getStoreName())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .build()));
    }

    /**
     * 매장 정보 수정
     * @param managerId - 매니저 인덱스
     * @param storeId - 매장 인덱스
     * @param dto - 매니저 인덱스, 매장 이름, 설명, 위치
     * @return
     */
    @Transactional
    public StoreDto edit(Long managerId, Long storeId, StoreDto dto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        validateMatchStoreToManager(managerId, store);

        store.edit(dto);

        return StoreDto.from(storeRepository.save(store));

    }

    /**
     * 매장 등록 해지
     * @param managerId - 매니저 인덱스
     * @param storeId - 매장 인덱스
     */
    @Transactional
    public void delete(Long managerId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE));

        if (!store.getManager().getId().equals(managerId)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_STORE_TO_MANAGER);
        }

        storeRepository.delete(store);
    }

    /**
     * 매장 상세 정보
     * @param storeId - 매장 인덱스
     */
    public StoreDto detail(Long storeId) {
        return StoreDto.from(storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_STORE)));
    }

    /**
     * 매니저 정보 가져오기
     */
    private Manager getManager(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MANAGER));
    }

    /**
     * 이미 매장을 등록한 매니저인지, 이미 등록된 매장인지 검증
     */
    private void validateRegister(Long managerId, String storeName) {
        if (storeRepository.existsByManagerId(managerId)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_MANAGER);
        }
        if (storeRepository.existsByStoreName(storeName)) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_STORE);
        }
    }


    /**
     * 매장의 매니저와 매장 정보를 수정하려는 매니저가 일치하는지 검증
     */
    private void validateMatchStoreToManager(Long managerId, Store store) {
        if (!store.getManager().getId().equals(managerId)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_STORE_TO_MANAGER);
        }
    }

}
