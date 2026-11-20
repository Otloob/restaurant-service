package com.outloob.restaurant.restaurant.mapper;


import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.restaurant.entity.Address;
import com.outloob.restaurant.restaurant.entity.OpeningHours;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantMapper {

    public static Restaurant toEntity(RestaurantRequestDto request) {
        if (request == null) {
            return null;
        }

       return Restaurant.builder()
                .name(request.name())
                .description(request.description())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .deliveryRadiusM(request.deliveryRadiusM())
                .address(request.address())
                .openingHours(null)
                .build();
    }

    public static RestaurantResponseDto toRestaurantDetialsResponseDto (Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getEmail(),
                restaurant.getPhoneNumber(),
                restaurant.getStatus(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getDeliveryRadiusM(),
                restaurant.getRating(),
                restaurant.getRatingCount(),
                restaurant.getAddress(),
                restaurant.getOpeningHours()
        );
    }
}
