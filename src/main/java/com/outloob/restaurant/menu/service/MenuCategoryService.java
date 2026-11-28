package com.outloob.restaurant.menu.service;

import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.menu.dto.MenuCategoryRequestDto;
import com.outloob.restaurant.menu.dto.MenuCategoryResponseDto;
import com.outloob.restaurant.menu.entity.MenuCategory;
import com.outloob.restaurant.menu.mapper.MenuMapper;
import com.outloob.restaurant.menu.repository.MenuCategoryRepository;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@AllArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuCategoryResponseDto createCategory(UUID restaurantId, MenuCategoryRequestDto request) {
        Restaurant restaurant = findRestaurant(restaurantId);
        MenuCategory category = MenuMapper.toEntity(request, restaurant);
        return MenuMapper.toResponseDto(menuCategoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public ApiResponse.PagedData<MenuCategoryResponseDto> getCategories(
            UUID restaurantId, int page, int limit) {
        ensureRestaurantExists(restaurantId);
        page = page < 0 ? 0 : page;
        limit = limit < 1 || limit > 100 ? 20 : limit;
        return ApiResponse.PagedData.from(menuCategoryRepository
                .findAllByRestaurant_Id(restaurantId, PageRequest.of(page, limit, Sort.by("id")))
                .map(MenuMapper::toResponseDto));
    }

    @Transactional(readOnly = true)
    public MenuCategoryResponseDto getCategory(UUID restaurantId, UUID categoryId) {
        return MenuMapper.toResponseDto(findCategory(restaurantId, categoryId));
    }

    @Transactional
    public MenuCategoryResponseDto updateCategory(
            UUID restaurantId, UUID categoryId, MenuCategoryRequestDto request) {
        MenuCategory category = findCategory(restaurantId, categoryId);
        category.setName(request.name());
        category.setDescription(request.description());
        return MenuMapper.toResponseDto(menuCategoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(UUID restaurantId, UUID categoryId) {
        menuCategoryRepository.delete(findCategory(restaurantId, categoryId));
    }

    private Restaurant findRestaurant(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Restaurant not found: " + restaurantId));
    }

    private void ensureRestaurantExists(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Restaurant not found: " + restaurantId);
        }
    }

    private MenuCategory findCategory(UUID restaurantId, UUID categoryId) {
        return menuCategoryRepository.findByIdAndRestaurant_Id(categoryId, restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Menu category not found: " + categoryId));
    }

}
