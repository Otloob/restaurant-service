package com.outloob.restaurant.menu.repository;

import com.outloob.restaurant.menu.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, UUID> {
    List<MenuCategory> findAllByRestaurant_Id(UUID restaurantId);

    Optional<MenuCategory> findByIdAndRestaurant_Id(UUID categoryId, UUID restaurantId);
}
