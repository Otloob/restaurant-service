package com.outloob.restaurant.menu.repository;

import com.outloob.restaurant.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, UUID> {
    Page<MenuCategory> findAllByRestaurant_Id(UUID restaurantId, Pageable pageable);

    Optional<MenuCategory> findByIdAndRestaurant_Id(UUID categoryId, UUID restaurantId);
}
