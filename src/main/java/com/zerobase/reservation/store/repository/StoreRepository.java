package com.zerobase.reservation.store.repository;

import com.zerobase.reservation.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    boolean existsByManagerId(Long id);

    boolean existsByStoreName(String storeName);
}
