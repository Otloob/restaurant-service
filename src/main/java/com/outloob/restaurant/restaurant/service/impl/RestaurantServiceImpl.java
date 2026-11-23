package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.common.exception.DuplicateResourceException;
import com.outloob.restaurant.common.exception.InvalidRequestException;
import com.outloob.restaurant.restaurant.dto.OpeningHoursDto;
import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDetailsDto;
import com.outloob.restaurant.restaurant.dto.UpdateOpeningHoursRequest;
import com.outloob.restaurant.restaurant.dto.UpdateRestaurantProfileRequest;
import com.outloob.restaurant.restaurant.entity.OpeningHours;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.mapper.RestaurantMapper;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.restaurant.service.RestaurantAccessService;
import com.outloob.restaurant.restaurant.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantAccessService restaurantAccessService;

    @Override
    @Transactional
    public RestaurantResponseDetailsDto createRestaurant(RestaurantRequestDto restaurantRequestDto, UUID ownerId) {
        if (ownerId == null) {
            throw new InvalidRequestException("X-User-Id is required.");
        }

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
        restaurant.setOwnerId(ownerId);
        restaurant.setEmail(email);
        restaurant.setPhoneNumber(phoneNumber);
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toRestaurantDetailsResponseDto(savedRestaurant);
    }

    @Override
    @Transactional
    public RestaurantResponseDetailsDto updateRestaurantProfile(UUID restaurantId,
                                                                UpdateRestaurantProfileRequest restaurantRequestDto,
                                                                UUID ownerId) {
        Restaurant restaurant = restaurantAccessService.getForMerchant(restaurantId, ownerId);

        String email = normalizeEmail(restaurantRequestDto.email());
        String phoneNumber = normalizePhoneNumber(restaurantRequestDto.phoneNumber());

        if (restaurantRepository.existsByEmailIgnoreCaseAndIdNot(email, restaurantId)) {
            throw new DuplicateResourceException(
                    "A restaurant with email '" + email + "' already exists");
        }

        if (restaurantRepository.existsByPhoneNumberAndIdNot(phoneNumber, restaurantId)) {
            throw new DuplicateResourceException(
                    "A restaurant with phone number '" + phoneNumber + "' already exists");
        }

        RestaurantMapper.updateEntity(restaurant, restaurantRequestDto);
        restaurant.setEmail(email);
        restaurant.setPhoneNumber(phoneNumber);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toRestaurantDetailsResponseDto(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponseDetailsDto getRestaurantDetails(UUID restaurantId, UUID ownerId) {
        Restaurant restaurant = restaurantAccessService.getForMerchant(restaurantId, ownerId);
        return RestaurantMapper.toRestaurantDetailsResponseDto(restaurant);
    }

    @Override
    @Transactional
    public RestaurantResponseDetailsDto updateOpeningHours(UUID restaurantId,
                                                           UpdateOpeningHoursRequest request,
                                                           UUID ownerId) {
        Restaurant restaurant = restaurantAccessService.getForMerchantForUpdate(restaurantId, ownerId);

        List<OpeningHoursDto> openingHours = request.openingHours();
        validateOpeningHours(openingHours);

        restaurant.getOpeningHours().clear();
        restaurant.getOpeningHours().addAll(RestaurantMapper.toOpeningHoursEntities(openingHours));

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return RestaurantMapper.toRestaurantDetailsResponseDto(savedRestaurant);
    }

    private static void validateOpeningHours(List<OpeningHoursDto> openingHours) {
        if (openingHours == null) {
            throw new InvalidRequestException("openingHours is required.");
        }

        Set<String> seen = new HashSet<>();
        for (OpeningHoursDto hours : openingHours) {
            if (hours.dayOfWeek() < 1 || hours.dayOfWeek() > 7) {
                throw new InvalidRequestException(
                        "dayOfWeek must be between 1 (Monday) and 7 (Sunday).");
            }

            if (hours.opensAt() == null || hours.closesAt() == null) {
                throw new InvalidRequestException("Opening and closing times are required.");
            }

            if (hours.opensAt().equals(hours.closesAt())) {
                throw new InvalidRequestException(
                        "Opening and closing time cannot be the same on day " + hours.dayOfWeek() + ".");
            }

            String key = hours.dayOfWeek() + "|" + hours.opensAt() + "|" + hours.closesAt();
            if (!seen.add(key)) {
                throw new InvalidRequestException(
                        "Duplicate opening hours entry on day " + hours.dayOfWeek() + ".");
            }
        }
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
