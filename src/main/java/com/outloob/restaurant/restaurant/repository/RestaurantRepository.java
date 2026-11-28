package com.outloob.restaurant.restaurant.repository;

import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, UUID id);

    boolean existsByPhoneNumberAndIdNot(String phoneNumber, UUID id);

    Optional<Restaurant> findByOwnerId(UUID ownerId);

    Optional<Restaurant> findByIdAndStatus(UUID id, RestaurantStatus status);

    Page<Restaurant> findAllByStatus(RestaurantStatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Restaurant r where r.id = :id")
    Optional<Restaurant> findByIdForUpdate(@Param("id") UUID id);
}
