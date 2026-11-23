package com.outloob.restaurant.restaurant.entity;


import com.outloob.restaurant.common.BaseEntity;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "restaurant",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_restaurant_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_restaurant_phone_number", columnNames = "phone_number")
        },
        indexes = @Index(name = "idx_restaurant_owner_id", columnList = "owner_id")
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Id of the authenticated account (auth-service user id) that owns this
     * restaurant. Taken from the authenticated principal (JWT `sub` /
     * gateway `X-User-Id` header). Never populated from the request body.
     */
    @Column(name = "owner_id", updatable = false)
    private UUID ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private RestaurantStatus status = RestaurantStatus.ACTIVE;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "delivery_radius_m", nullable = false)
    @Builder.Default
    private int deliveryRadiusM = 5000;

    @Column(name = "rating", nullable = false)
    @Builder.Default
    private double rating = 5.0;

    @Column(name = "rating_count", nullable = false)
    @Builder.Default
    private int ratingCount = 0;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayOfWeek ASC, opensAt ASC")
    @Builder.Default
    private List<OpeningHours> openingHours = new ArrayList<>();

}
