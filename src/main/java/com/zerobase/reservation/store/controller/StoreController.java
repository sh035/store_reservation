package com.zerobase.reservation.store.controller;

import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.store.dto.StoreDto;
import com.zerobase.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    /**
     * 매장 등록
     * @param manager - 로그인된 정보 (매니저)
     * @param dto - 매니저 인덱스, 매장 이름, 설명, 위치
     */
    @PostMapping("/manager/register")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<?> registerStore(
            @AuthenticationPrincipal Manager manager, @RequestBody StoreDto dto) {

        StoreDto store = storeService.register(manager.getId(), dto);
        return ResponseEntity.ok().body(store);
    }

    /**
     * 매장 정보 수정
     * @param manager - 로그인된 정보 (매니저)
     * @param storeId - 매장 인덱스
     * @param dto - 매니저 인덱스, 매장 이름, 설명, 위치
     */
    @PutMapping("/manager/edit/{storeId}")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<?> editStore(@AuthenticationPrincipal Manager manager,
                                       @PathVariable Long storeId, @RequestBody StoreDto dto) {
        StoreDto store = storeService.edit(manager.getId(), storeId, dto);
        return ResponseEntity.ok().body(store);
    }

    /**
     * 매장 등록 해지
     * @param manager - 로그인된 정보 (매니저)
     * @param storeId - 매장 인덱스
     */
    @DeleteMapping("/manager/delete")
    @PreAuthorize("hasRole('ROLE_PARTNER')")
    public ResponseEntity<?> deleteStore(@AuthenticationPrincipal Manager manager,
                                         @PathVariable Long storeId) {
        storeService.delete(manager.getId(), storeId);
        return ResponseEntity.ok("매장 등록을 해지하였습니다.");
    }

    /**
     * 매장 상세 정보
     * @param storeId - 매장 인덱스
     */
    @GetMapping("/detail/{storeId}")
    public ResponseEntity<?> detailStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.detail(storeId));
    }
}
