package com.zerobase.reservation.store.dto;

import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.store.entity.Store;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {
    private Long managerId;
    private String storeName;
    private String description;
    private String location;

    public static StoreDto from(Store store) {
        return StoreDto.builder()
                .managerId(store.getManager().getId())
                .storeName(store.getStoreName())
                .description(store.getDescription())
                .location(store.getLocation())
                .build();
    }
}
