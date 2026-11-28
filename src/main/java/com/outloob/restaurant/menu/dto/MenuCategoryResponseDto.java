package com.outloob.restaurant.menu.dto;

import com.outloob.restaurant.menu.entity.MenuItem;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

public record MenuCategoryResponseDto (
         UUID id,
 String name,
 String description
){
}
