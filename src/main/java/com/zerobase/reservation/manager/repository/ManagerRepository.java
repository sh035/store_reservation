package com.zerobase.reservation.manager.repository;

import com.zerobase.reservation.manager.entity.Manager;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByEmail(String email);

    boolean existsByEmail(String email);
}
