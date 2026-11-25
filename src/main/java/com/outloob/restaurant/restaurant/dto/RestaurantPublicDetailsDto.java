package com.outloob.restaurant.restaurant.dto;

import com.outloob.restaurant.restaurant.entity.Address;

import java.util.List;
import java.util.UUID;

/**
 * Customer-facing restaurant representation used by the public details endpoint.
 * Internal fields such as ownerId, email and status are intentionally omitted.
 */
public record RestaurantPublicDetailsDto(
        UUID id,
        String name,
        String description,
        String phoneNumber,
        double latitude,
        double longitude,
        int deliveryRadiusM,
        double rating,
        int ratingCount,
        Address address,
        List<OpeningHoursDto> openingHours
) {
}
