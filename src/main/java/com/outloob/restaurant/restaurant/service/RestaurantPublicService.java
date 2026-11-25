package com.outloob.restaurant.restaurant.service;

import com.outloob.restaurant.restaurant.dto.RestaurantPublicDetailsDto;
import com.outloob.restaurant.restaurant.dto.RestaurantPublicSummaryDto;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RestaurantPublicService {

    Page<RestaurantPublicSummaryDto> getRestaurants(int page, int size);

    RestaurantPublicDetailsDto getRestaurantDetails(UUID restaurantId);
}