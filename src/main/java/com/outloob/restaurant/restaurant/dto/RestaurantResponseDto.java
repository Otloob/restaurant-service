package com.outloob.restaurant.restaurant.dto;

import com.outloob.restaurant.restaurant.entity.Address;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;

import java.util.List;
import java.util.UUID;

public record RestaurantResponseDto(
        UUID id,
        String name,
        String description,
        String email,
        String phoneNumber,
        RestaurantStatus status,
        double latitude,
        double longitude,
        int deliveryRadiusM,
        double rating,
        int ratingCount,
        Address address,
        List<OpeningHoursResponseDto> openingHours
) {
}
