package com.outloob.restaurant.restaurant.service;

import com.outloob.restaurant.restaurant.entity.Restaurant;

import java.util.UUID;

public interface RestaurantAccessService {

    Restaurant getForMerchant(UUID restaurantId, UUID ownerId);

    Restaurant getForMerchantForUpdate(UUID restaurantId, UUID ownerId);

    Restaurant getRestaurant(UUID restaurantId);
}
