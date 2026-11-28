package com.outloob.restaurant.search.repository;

import com.outloob.restaurant.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantNearbyQueryRepository {
    Page<Restaurant> findNearby(String status, double latitude, double longitude, double radiusMeters,
                                String restaurantName, String categoryName, String dishName, String tag,
                                Pageable pageable);
}
