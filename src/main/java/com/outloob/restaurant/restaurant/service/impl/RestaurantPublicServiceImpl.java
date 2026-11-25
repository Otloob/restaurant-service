package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.common.exception.InvalidRequestException;
import com.outloob.restaurant.common.exception.ResourceNotFoundException;
import com.outloob.restaurant.restaurant.dto.RestaurantPublicDetailsDto;
import com.outloob.restaurant.restaurant.dto.RestaurantPublicSummaryDto;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.mapper.RestaurantMapper;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.restaurant.service.RestaurantPublicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RestaurantPublicServiceImpl implements RestaurantPublicService {

    private static final int MAX_PAGE_SIZE = 100;

    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<RestaurantPublicSummaryDto> getRestaurants(int page, int size) {
        if (page < 0) {
            throw new InvalidRequestException("page must be greater than or equal to 0.");
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidRequestException(
                    "size must be between 1 and " + MAX_PAGE_SIZE + ".");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.desc("rating"),
                Sort.Order.desc("ratingCount"),
                Sort.Order.asc("id")));

        return restaurantRepository
                .findAllByStatus(RestaurantStatus.ACTIVE, pageable)
                .map(RestaurantMapper::toPublicSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantPublicDetailsDto getRestaurantDetails(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository
                .findByIdAndStatus(restaurantId, RestaurantStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found."));
        return RestaurantMapper.toPublicDetailsDto(restaurant);
    }
}
