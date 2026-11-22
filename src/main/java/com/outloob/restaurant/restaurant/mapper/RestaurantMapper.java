package com.outloob.restaurant.restaurant.mapper;


import com.outloob.restaurant.restaurant.dto.OpeningHoursDto;
import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDetailsDto;
import com.outloob.restaurant.restaurant.dto.UpdateRestaurantProfileRequest;
import com.outloob.restaurant.restaurant.entity.OpeningHours;
import com.outloob.restaurant.restaurant.entity.Restaurant;

import java.util.List;

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
                .build();
    }

    public static void updateEntity(Restaurant restaurant, UpdateRestaurantProfileRequest request) {
        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setEmail(request.email());
        restaurant.setPhoneNumber(request.phoneNumber());
        restaurant.setLatitude(request.latitude());
        restaurant.setLongitude(request.longitude());
        restaurant.setDeliveryRadiusM(request.deliveryRadiusM());
        restaurant.setAddress(request.address());
    }

    public static RestaurantResponseDetailsDto toRestaurantDetailsResponseDto(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantResponseDetailsDto(
                restaurant.getId(),
                restaurant.getOwnerId(),
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
                toOpeningHoursDtos(restaurant.getOpeningHours())
        );
    }

    private static List<OpeningHoursDto> toOpeningHoursDtos(List<OpeningHours> openingHours) {
        if (openingHours == null) {
            return List.of();
        }

        return openingHours.stream()
                .map(openingHoursEntity -> new OpeningHoursDto(
                        openingHoursEntity.getDayOfWeek(),
                        openingHoursEntity.getOpensAt(),
                        openingHoursEntity.getClosesAt()))
                .toList();
    }
}
