package com.outloob.restaurant.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.UUID;

public record MenuItemRequestDto(
        @NotNull
        UUID categoryId,

        @NotNull
        String name,
        String description,

        @NotNull
        Double price,

        String imageUrl,
        Boolean available
) {
    public MenuItemRequestDto{
       if(available == null){
            available = true;
        }
    }

}
