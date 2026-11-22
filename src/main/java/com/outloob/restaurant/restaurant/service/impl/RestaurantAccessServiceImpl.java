package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.common.exception.ResourceNotFoundException;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.restaurant.service.RestaurantAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantAccessServiceImpl implements RestaurantAccessService {

    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public Restaurant getForMerchant(UUID restaurantId, UUID ownerId) {
        Restaurant restaurant = getRestaurant(restaurantId);
        checkMembership(restaurant, ownerId);
        return restaurant;
    }

    @Override
    @Transactional
    public Restaurant getForMerchantForUpdate(UUID restaurantId, UUID ownerId) {
        Restaurant restaurant = restaurantRepository.findByIdForUpdate(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found."));
        checkMembership(restaurant, ownerId);
        return restaurant;
    }

    @Override
    @Transactional(readOnly = true)
    public Restaurant getRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found."));
    }

    private void checkMembership(Restaurant restaurant, UUID ownerId) {
        if (ownerId == null || !ownerId.equals(restaurant.getOwnerId())) {
            throw new ResourceNotFoundException("Restaurant not found.");
        }
    }
}
