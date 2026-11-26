package com.outloob.restaurant.menu.service;

import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.menu.dto.MenuItemRequestDto;
import com.outloob.restaurant.menu.dto.MenuItemResponseDto;
import com.outloob.restaurant.menu.entity.MenuCategory;
import com.outloob.restaurant.menu.entity.MenuItem;
import com.outloob.restaurant.menu.mapper.MenuItemMapper;
import com.outloob.restaurant.menu.repository.MenuCategoryRepository;
import com.outloob.restaurant.menu.repository.MenuItemRepository;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@AllArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuItemResponseDto createItem(UUID restaurantId, MenuItemRequestDto request) {
        MenuCategory category = findCategory(restaurantId, request.categoryId());
        MenuItem item = MenuItemMapper.toEntity(request, category);
        return MenuItemMapper.toResponseDto(menuItemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public ApiResponse.PagedData<MenuItemResponseDto> getItems(
            UUID restaurantId, UUID categoryId, int page, int limit) {
        ensureRestaurantExists(restaurantId);
        if (categoryId != null) {
            findCategory(restaurantId, categoryId);
        }
        var pageable = PageRequest.of(page, limit, Sort.by("id"));
        var items = categoryId == null
                ? menuItemRepository.findAllByCategory_Restaurant_Id(restaurantId, pageable)
                : menuItemRepository.findAllByCategory_IdAndCategory_Restaurant_Id(
                        categoryId, restaurantId, pageable);
        return ApiResponse.PagedData.from(items.map(MenuItemMapper::toResponseDto));
    }

    @Transactional(readOnly = true)
    public MenuItemResponseDto getItem(UUID restaurantId, UUID itemId) {
        return MenuItemMapper.toResponseDto(findItem(restaurantId, itemId));
    }

    @Transactional
    public MenuItemResponseDto updateItem(UUID restaurantId, UUID itemId, MenuItemRequestDto request) {
        MenuItem item = findItem(restaurantId, itemId);
        MenuCategory category = findCategory(restaurantId, request.categoryId());
        MenuItemMapper.updateEntity(item, request, category);
        return MenuItemMapper.toResponseDto(menuItemRepository.save(item));
    }

    @Transactional
    public void deleteItem(UUID restaurantId, UUID itemId) {
        menuItemRepository.delete(findItem(restaurantId, itemId));
    }

    private MenuItem findItem(UUID restaurantId, UUID itemId) {
        return menuItemRepository.findByIdAndCategory_Restaurant_Id(itemId, restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Menu item not found: " + itemId));
    }

    private MenuCategory findCategory(UUID restaurantId, UUID categoryId) {
        if (categoryId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category ID is required");
        }
        return menuCategoryRepository.findByIdAndRestaurant_Id(categoryId, restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Menu category not found: " + categoryId));
    }

    private void ensureRestaurantExists(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Restaurant not found: " + restaurantId);
        }
    }

}
