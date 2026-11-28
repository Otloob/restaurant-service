package com.outloob.restaurant.menu.repository;

import com.outloob.restaurant.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    Page<MenuItem> findAllByCategory_Restaurant_Id(UUID restaurantId, Pageable pageable);

    Page<MenuItem> findAllByCategory_IdAndCategory_Restaurant_Id(
            UUID categoryId, UUID restaurantId, Pageable pageable);

    Optional<MenuItem> findByIdAndCategory_Restaurant_Id(UUID itemId, UUID restaurantId);
}
