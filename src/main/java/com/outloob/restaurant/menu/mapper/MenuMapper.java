package com.outloob.restaurant.menu.mapper;

import com.outloob.restaurant.menu.dto.MenuCategoryRequestDto;
import com.outloob.restaurant.menu.dto.MenuCategoryResponseDto;
import com.outloob.restaurant.menu.entity.MenuCategory;
import com.outloob.restaurant.restaurant.entity.Restaurant;

public class MenuMapper {

    public static MenuCategory toEntity(MenuCategoryRequestDto request, Restaurant restaurant) {
        return MenuCategory.builder()
                .name(request.name())
                .description(request.description())
                .restaurant(restaurant)
                .build();
    }

    public static MenuCategoryResponseDto toResponseDto(MenuCategory category) {
        return new MenuCategoryResponseDto(category.getId(), category.getName(), category.getDescription());
    }
}
