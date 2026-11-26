package com.outloob.restaurant.search.controller;

import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/restaurants")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<ApiResponse.PagedData<RestaurantResponseDto>>> findNearbyRestaurants(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5") double radiusKm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        ApiResponse.PagedData<RestaurantResponseDto> results =
                searchService.findNearbyRestaurants(latitude, longitude, radiusKm, page, limit);
        return ResponseEntity.ok(ApiResponse.success(results, "Nearby restaurants found"));
    }
}
