package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.common.exception.DuplicateResourceException;
import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.mapper.RestaurantMapper;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@AllArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
        String email = normalizeEmail(restaurantRequestDto.email());
        String phoneNumber = normalizePhoneNumber(restaurantRequestDto.phoneNumber());

        if (restaurantRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException(
                    "A restaurant with email '" + email + "' already exists");
        }

        if (restaurantRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicateResourceException(
                    "A restaurant with phone number '" + phoneNumber + "' already exists");
        }

        Restaurant restaurant = RestaurantMapper.toEntity(restaurantRequestDto);
        restaurant.setEmail(email);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toRestaurantDetailsResponseDto(savedRestaurant);
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizePhoneNumber(String phoneNumber) {
        String trimmed = phoneNumber.trim();
        boolean international = trimmed.startsWith("+");
        String digits = trimmed.replaceAll("[^0-9]", "");
        return international ? "+" + digits : digits;
    }
}
