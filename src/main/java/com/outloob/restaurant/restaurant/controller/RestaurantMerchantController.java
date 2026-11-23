package com.outloob.restaurant.restaurant.controller;

import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDetailsDto;
import com.outloob.restaurant.restaurant.dto.UpdateOpeningHoursRequest;
import com.outloob.restaurant.restaurant.dto.UpdateRestaurantProfileRequest;
import com.outloob.restaurant.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/merchants/restaurants")
@AllArgsConstructor
public class RestaurantMerchantController {

    private final RestaurantService restaurantService;

    @PostMapping(version = "1.0")
    public ResponseEntity<ApiResponse<RestaurantResponseDetailsDto>> createRestaurant(
            @Valid @RequestBody RestaurantRequestDto restaurant,
            // Injected by the API gateway after JWT validation.
            // TODO: read it from the authenticated principal once security is in place.
            @RequestHeader(value = "X-User-Id", required = false) UUID ownerId) {
        RestaurantResponseDetailsDto createdRestaurant = restaurantService.createRestaurant(restaurant, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdRestaurant,
                        "Restaurant registered successfully and is pending admin approval"));
    }

    @PutMapping(value = "/{restaurantId}", version = "1.0")
    public ResponseEntity<ApiResponse<RestaurantResponseDetailsDto>> updateRestaurantProfile(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody UpdateRestaurantProfileRequest restaurant,
            @RequestHeader(value = "X-User-Id", required = false) UUID ownerId) {
        RestaurantResponseDetailsDto updatedRestaurant =
                restaurantService.updateRestaurantProfile(restaurantId, restaurant, ownerId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(updatedRestaurant, "Restaurant profile updated successfully"));
    }

    @GetMapping(value = "/{restaurantId}", version = "1.0")
    public ResponseEntity<ApiResponse<RestaurantResponseDetailsDto>> getRestaurantDetails(
            @PathVariable UUID restaurantId,
            @RequestHeader(value = "X-User-Id", required = false) UUID ownerId) {
        RestaurantResponseDetailsDto restaurant = restaurantService.getRestaurantDetails(restaurantId, ownerId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(restaurant, "Restaurant details retrieved successfully"));
    }

    @PutMapping(value = "/{restaurantId}/opening-hours", version = "1.0")
    public ResponseEntity<ApiResponse<RestaurantResponseDetailsDto>> updateOpeningHours(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody UpdateOpeningHoursRequest request,
            @RequestHeader(value = "X-User-Id", required = false) UUID ownerId) {
        RestaurantResponseDetailsDto restaurant =
                restaurantService.updateOpeningHours(restaurantId, request, ownerId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(restaurant, "Opening hours updated successfully"));
    }
}
