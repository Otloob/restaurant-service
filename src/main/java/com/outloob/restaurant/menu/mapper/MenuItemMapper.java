package com.outloob.restaurant.menu.mapper;

import com.outloob.restaurant.menu.dto.MenuItemRequestDto;
import com.outloob.restaurant.menu.dto.MenuItemResponseDto;
import com.outloob.restaurant.menu.entity.MenuCategory;
import com.outloob.restaurant.menu.entity.MenuItem;

public class MenuItemMapper {

    public static MenuItem toEntity(MenuItemRequestDto request, MenuCategory category) {
        return MenuItem.builder()
                .category(category)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .available(request.available())
                .build();
    }

    public static void updateEntity(MenuItem item, MenuItemRequestDto request, MenuCategory category) {
        item.setCategory(category);
        item.setName(request.name());
        item.setDescription(request.description());
        item.setPrice(request.price());
        item.setImageUrl(request.imageUrl());
        item.setAvailable(request.available());
    }

    public static MenuItemResponseDto toResponseDto(MenuItem item) {
        return new MenuItemResponseDto(item.getId(), item.getCategory().getId(), item.getName(),
                item.getDescription(), item.getPrice(), item.getImageUrl(), item.getAvailable());
    }
}
