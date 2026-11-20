package com.outloob.restaurant.restaurant.service;

import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;

public interface RestaurantService {
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto);
}
