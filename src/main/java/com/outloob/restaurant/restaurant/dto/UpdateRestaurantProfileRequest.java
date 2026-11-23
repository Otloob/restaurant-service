package com.outloob.restaurant.restaurant.dto;

import com.outloob.restaurant.restaurant.entity.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UpdateRestaurantProfileRequest(
        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 1000)
        String description,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=[+0-9\\s\\-()]*[0-9])\\+?[0-9\\s\\-()]{7,20}$",
                message = "phoneNumber must be a valid phone number (7-20 characters, digits with optional +, spaces, dashes or parentheses)"
        )
        String phoneNumber,

        @Min(value = -90, message = "latitude must be between -90 and 90")
        @Max(value = 90, message = "latitude must be between -90 and 90")
        double latitude,

        @Min(value = -180, message = "longitude must be between -180 and 180")
        @Max(value = 180, message = "longitude must be between -180 and 180")
        double longitude,

        @Positive(message = "deliveryRadiusM must be greater than 0")
        int deliveryRadiusM,

        @NotNull
        @Valid
        Address address
) implements Serializable {
}
