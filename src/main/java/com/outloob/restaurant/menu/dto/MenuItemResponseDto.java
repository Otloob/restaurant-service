package com.outloob.restaurant.menu.dto;

import java.util.UUID;

public record MenuItemResponseDto(
        UUID id,
        UUID categoryId,
        String name,
        String description,
        Double price,
        String imageUrl,
        Boolean available
) {
}
