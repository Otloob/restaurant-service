package com.outloob.restaurant.search.service;

import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.mapper.RestaurantMapper;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private static final double EARTH_RADIUS_KM = 6371.0088;

    private final RestaurantRepository restaurantRepository;

    public ApiResponse.PagedData<RestaurantResponseDto> findNearbyRestaurants(
            double latitude, double longitude, double radiusKm, int page, int limit) {
        if (!isValidCoordinate(latitude, longitude) || !Double.isFinite(radiusKm)
                || radiusKm <= 0 || page < 0 || limit < 1 || limit > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Coordinates must be valid, radiusKm must be positive, page must be nonnegative, and limit must be between 1 and 100");
        }

        List<Restaurant> nearby = restaurantRepository.findAll().stream()
                .filter(restaurant -> restaurant.getStatus() == RestaurantStatus.ACTIVE)
                .filter(restaurant -> distanceKm(latitude, longitude,
                        restaurant.getLatitude(), restaurant.getLongitude()) <= radiusKm)
                .sorted(Comparator
                        .comparingDouble((Restaurant restaurant) -> distanceKm(latitude, longitude,
                                restaurant.getLatitude(), restaurant.getLongitude()))
                        .thenComparing(Restaurant::getId))
                .toList();

        int startIndex = (int) Math.min((long) page * limit, nearby.size());
        int endIndex = Math.min(startIndex + limit, nearby.size());
        List<RestaurantResponseDto> content = nearby.subList(startIndex, endIndex).stream()
                .map(RestaurantMapper::toRestaurantDetialsResponseDto)
                .toList();
        return ApiResponse.PagedData.from(
                new PageImpl<>(content, PageRequest.of(page, limit), nearby.size()));
    }

    private boolean isValidCoordinate(double latitude, double longitude) {
        return Double.isFinite(latitude) && Double.isFinite(longitude)
                && latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180;
    }

    private double distanceKm(double latitude1, double longitude1,
                              double latitude2, double longitude2) {
        double lat1 = Math.toRadians(latitude1);
        double lat2 = Math.toRadians(latitude2);
        double deltaLat = Math.toRadians(latitude2 - latitude1);
        double deltaLon = Math.toRadians(longitude2 - longitude1);
        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
