package com.outloob.restaurant.restaurant.service;

import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDetailsDto;
import com.outloob.restaurant.restaurant.dto.UpdateRestaurantProfileRequest;

import java.util.UUID;

public interface RestaurantService {
    RestaurantResponseDetailsDto createRestaurant(RestaurantRequestDto restaurantRequestDto, UUID ownerId);

    RestaurantResponseDetailsDto updateRestaurantProfile(UUID restaurantId,
                                                         UpdateRestaurantProfileRequest restaurantRequestDto,
                                                         UUID ownerId);

    RestaurantResponseDetailsDto getRestaurantDetails(UUID restaurantId, UUID ownerId);
}
