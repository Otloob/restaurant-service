package com.outloob.restaurant.restaurant.dto;

import com.outloob.restaurant.restaurant.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record RestaurantRequestDto(
        @NotNull @NotEmpty
        String name,

        @NotEmpty
        String description,

        @NotNull @Email @NotEmpty
        String email,

        String phoneNumber,

        double latitude,

        double longitude,

        int deliveryRadiusM,

        @NotNull
        Address address
) implements Serializable {
}