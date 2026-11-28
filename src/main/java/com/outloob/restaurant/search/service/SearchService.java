package com.outloob.restaurant.search.service;

import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.mapper.RestaurantMapper;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.search.repository.RestaurantNearbyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantNearbyQueryRepository nearbyQueryRepository;

    public ApiResponse.PagedData<RestaurantResponseDto> findNearbyRestaurants(
            double latitude, double longitude, double radiusKm, String restaurantName,
            String category, String dish, String tag, int page, int limit) {
        if (!isValidCoordinate(latitude, longitude) || !Double.isFinite(radiusKm)
                || radiusKm <= 0 || page < 1 || limit < 1 || limit > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Coordinates must be valid, radiusKm must be positive, page must be at least 1, and limit must be between 1 and 100");
        }

        Page<Restaurant> nearby = nearbyQueryRepository.findNearby(
                RestaurantStatus.ACTIVE.name(), latitude, longitude, radiusKm * 1000,
                normalizeFilter(restaurantName), normalizeFilter(category),
                normalizeFilter(dish), normalizeFilter(tag), PageRequest.of(page - 1, limit));
        List<RestaurantResponseDto> content = nearby.getContent().stream()
                .map(RestaurantMapper::toRestaurantDetialsResponseDto)
                .toList();
        return new ApiResponse.PagedData<>(
                content,
                new ApiResponse.PageMetadata(
                        page, limit, nearby.getTotalElements(), nearby.getTotalPages(), nearby.isLast()));
    }

    public ApiResponse.PagedData<RestaurantResponseDto> searchRestaurants(
            String restaurantName, String category, String dish, String tag, int page, int limit) {
        validatePagination(page, limit);
        Page<Restaurant> results = restaurantRepository.searchRestaurants(
                RestaurantStatus.ACTIVE,
                normalizeFilter(restaurantName),
                normalizeFilter(category),
                normalizeFilter(dish),
                normalizeFilter(tag),
                PageRequest.of(page - 1, limit, Sort.by("id")));
        List<RestaurantResponseDto> content = results.getContent().stream()
                .map(RestaurantMapper::toRestaurantDetialsResponseDto)
                .toList();
        return new ApiResponse.PagedData<>(
                content,
                new ApiResponse.PageMetadata(
                        page, limit, results.getTotalElements(), results.getTotalPages(), results.isLast()));
    }

    private String normalizeFilter(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private boolean isValidCoordinate(double latitude, double longitude) {
        return Double.isFinite(latitude) && Double.isFinite(longitude)
                && latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180;
    }

    private void validatePagination(int page, int limit) {
        if (page < 1 || limit < 1 || limit > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Page must be at least 1 and limit must be between 1 and 100");
        }
    }

}
