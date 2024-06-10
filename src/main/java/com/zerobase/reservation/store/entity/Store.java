package com.zerobase.reservation.store.entity;

import com.zerobase.reservation.global.entity.BaseEntity;
import com.zerobase.reservation.manager.entity.Manager;
import com.zerobase.reservation.store.dto.StoreDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    private String storeName;

    private String description;

    private String location;


    public void edit(StoreDto dto) {
        this.storeName = dto.getStoreName();
        this.description = dto.getDescription();
        this.location = dto.getLocation();
    }
}
