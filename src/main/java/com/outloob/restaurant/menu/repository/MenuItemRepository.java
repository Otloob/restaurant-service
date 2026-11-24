package com.outloob.restaurant.menu.repository;

import com.outloob.restaurant.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> findAllByCategory_Restaurant_Id(UUID restaurantId);

    Optional<MenuItem> findByIdAndCategory_Restaurant_Id(UUID itemId, UUID restaurantId);
}
