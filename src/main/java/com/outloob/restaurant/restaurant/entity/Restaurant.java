package com.outloob.restaurant.restaurant.entity;


import com.outloob.restaurant.common.BaseEntity;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "restaurant")
@Getter
@Setter

public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "cuisine_type", nullable = false)
    private String cuisineType;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RestaurantStatus status = RestaurantStatus.ACTIVE;

    @Embedded
    private Address address;

    @Embedded
    private OperatingHours operatingHours;

}
