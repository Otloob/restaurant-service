package com.outloob.restaurant.menu.mapper;

import com.outloob.restaurant.menu.dto.MenuItemRequestDto;
import com.outloob.restaurant.menu.dto.MenuItemResponseDto;
import com.outloob.restaurant.menu.entity.MenuCategory;
import com.outloob.restaurant.menu.entity.MenuItem;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MenuItemMapperTest {

    @Test
    void toEntityMapsRequestFieldsAndCategory() {
        UUID categoryId = UUID.randomUUID();
        MenuCategory category = MenuCategory.builder().id(categoryId).build();
        MenuItemRequestDto request = new MenuItemRequestDto(
                categoryId, "Latte", "Milk coffee", 4.5, "latte.jpg", false);

        MenuItem item = MenuItemMapper.toEntity(request, category);

        assertNull(item.getId());
        assertEquals(category, item.getCategory());
        assertEquals("Latte", item.getName());
        assertEquals("Milk coffee", item.getDescription());
        assertEquals(4.5, item.getPrice());
        assertEquals("latte.jpg", item.getImageUrl());
        assertEquals(false, item.getAvailable());
    }

    @Test
    void toEntityPreservesRequestDefaultAvailability() {
        UUID categoryId = UUID.randomUUID();
        MenuCategory category = MenuCategory.builder().id(categoryId).build();
        MenuItemRequestDto request = new MenuItemRequestDto(
                categoryId, "Latte", null, 4.5, null, null);

        MenuItem item = MenuItemMapper.toEntity(request, category);

        assertEquals(true, item.getAvailable());
    }

    @Test
    void toResponseDtoMapsEntityFields() {
        UUID itemId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        MenuCategory category = MenuCategory.builder().id(categoryId).build();
        MenuItem item = MenuItem.builder()
                .id(itemId)
                .category(category)
                .name("Latte")
                .description("Milk coffee")
                .price(4.5)
                .imageUrl("latte.jpg")
                .available(true)
                .build();

        MenuItemResponseDto response = MenuItemMapper.toResponseDto(item);

        assertEquals(itemId, response.id());
        assertEquals(categoryId, response.categoryId());
        assertEquals("Latte", response.name());
        assertEquals("Milk coffee", response.description());
        assertEquals(4.5, response.price());
        assertEquals("latte.jpg", response.imageUrl());
        assertEquals(true, response.available());
    }
}
