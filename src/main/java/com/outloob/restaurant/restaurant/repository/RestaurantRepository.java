package com.outloob.restaurant.restaurant.repository;

import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Query(value = """
            SELECT r
            FROM Restaurant r
            WHERE r.status = :status
              AND (:restaurantName IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :restaurantName, '%')))
              AND (:categoryName IS NULL OR EXISTS (
                    SELECT c.id FROM MenuCategory c
                    WHERE c.restaurant = r
                      AND LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))
              ))
              AND ((:dishName IS NULL AND :tag IS NULL) OR EXISTS (
                    SELECT i.id FROM MenuItem i
                    WHERE i.category.restaurant = r
                      AND (:dishName IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :dishName, '%')))
                      AND (:tag IS NULL OR LOWER(i.tag) LIKE LOWER(CONCAT('%', :tag, '%')))
              ))
            """,
            countQuery = """
            SELECT COUNT(r)
            FROM Restaurant r
            WHERE r.status = :status
              AND (:restaurantName IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :restaurantName, '%')))
              AND (:categoryName IS NULL OR EXISTS (
                    SELECT c.id FROM MenuCategory c
                    WHERE c.restaurant = r
                      AND LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))
              ))
              AND ((:dishName IS NULL AND :tag IS NULL) OR EXISTS (
                    SELECT i.id FROM MenuItem i
                    WHERE i.category.restaurant = r
                      AND (:dishName IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :dishName, '%')))
                      AND (:tag IS NULL OR LOWER(i.tag) LIKE LOWER(CONCAT('%', :tag, '%')))
              ))
            """)
    Page<Restaurant> searchRestaurants(
            @Param("status") RestaurantStatus status,
            @Param("restaurantName") String restaurantName,
            @Param("categoryName") String categoryName,
            @Param("dishName") String dishName,
            @Param("tag") String tag,
            Pageable pageable);
}
