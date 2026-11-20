package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.mapper.RestaurantMapper;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    @Override
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = RestaurantMapper.toEntity(restaurantRequestDto);
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);
        restaurantRepository.save(restaurant);

        return RestaurantMapper.toRestaurantDetialsResponseDto(restaurant);
    }
}
