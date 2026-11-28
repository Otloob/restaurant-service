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

import java.util.Map;

import static com.outloob.restaurant.common.util.RequestValueUtils.optionalParam;
import static com.outloob.restaurant.common.util.RequestValueUtils.requestDouble;
import static com.outloob.restaurant.common.util.RequestValueUtils.requestInt;

@RestController
@RequestMapping("/v1/restaurants")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<ApiResponse.PagedData<RestaurantResponseDto>>> findNearbyRestaurants(
            @RequestParam Map<String, String> requestParams) {
        ApiResponse.PagedData<RestaurantResponseDto> results =
                searchService.findNearbyRestaurants(
                        requestDouble(requestParams, "latitude"), requestDouble(requestParams, "longitude"),
                        requestDouble(requestParams, "radiusKm", 5), optionalParam(requestParams, "restaurantName"),
                        optionalParam(requestParams, "category"), optionalParam(requestParams, "dish"),
                        optionalParam(requestParams, "tag"), requestInt(requestParams, "page", 1),
                        requestInt(requestParams, "limit", 20));
        return ResponseEntity.ok(ApiResponse.success(results, "Nearby restaurants found"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ApiResponse.PagedData<RestaurantResponseDto>>> searchRestaurants(
            @RequestParam Map<String, String> requestParams) {
        ApiResponse.PagedData<RestaurantResponseDto> results =
                searchService.searchRestaurants(optionalParam(requestParams, "restaurantName"),
                        optionalParam(requestParams, "category"), optionalParam(requestParams, "dish"),
                        optionalParam(requestParams, "tag"), requestInt(requestParams, "page", 1),
                        requestInt(requestParams, "limit", 20));
        return ResponseEntity.ok(ApiResponse.success(results, "Restaurants found"));
    }
}
