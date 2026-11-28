package com.outloob.restaurant.restaurant.controller;

import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.restaurant.dto.RestaurantPublicDetailsDto;
import com.outloob.restaurant.restaurant.dto.RestaurantPublicSummaryDto;
import com.outloob.restaurant.restaurant.service.RestaurantPublicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/restaurants")
@AllArgsConstructor
public class RestaurantPublicController {

    private final RestaurantPublicService restaurantPublicService;

    @GetMapping(version = "1.0")
    public ResponseEntity<ApiResponse<ApiResponse.PagedData<RestaurantPublicSummaryDto>>> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<RestaurantPublicSummaryDto> restaurants =
                restaurantPublicService.getRestaurants(page, size);

        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        ApiResponse.PagedData.from(restaurants),
                        "Restaurants retrieved successfully"));
    }

    @GetMapping(value = "/{restaurantId}", version = "1.0")
    public ResponseEntity<ApiResponse<RestaurantPublicDetailsDto>> getRestaurantDetails(
            @PathVariable UUID restaurantId) {
        RestaurantPublicDetailsDto restaurant =
                restaurantPublicService.getRestaurantDetails(restaurantId);

        return ResponseEntity.ok()
                .body(ApiResponse.success(
                        restaurant,
                        "Restaurant details retrieved successfully"));
    }
}
