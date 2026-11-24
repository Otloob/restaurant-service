package com.outloob.restaurant.menu.controller;


import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.menu.dto.MenuCategoryResponseDto;
import com.outloob.restaurant.menu.dto.MenuCategoryRequestDto;
import com.outloob.restaurant.menu.dto.MenuItemRequestDto;
import com.outloob.restaurant.menu.dto.MenuItemResponseDto;
import com.outloob.restaurant.menu.service.MenuCategoryService;
import com.outloob.restaurant.menu.service.MenuItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.List;

/*
CRUD
/api/v1/merchant/restaurants/{id}/categories[/{catId}]
Categories
CRUD
/api/v1/merchant/restaurants/{id}/items[/{itemId}]
Items
 */
@RestController
@RequestMapping("/merchant/restaurants/{id}")
@AllArgsConstructor
public class MenuController {

    private final MenuCategoryService menuCategoryService;
    private final MenuItemService menuItemService;

    @PostMapping(path = "/categories", version = "1.0")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDto>> createMenuCategory(
            @PathVariable UUID id,
            @RequestBody MenuCategoryRequestDto request) {
        MenuCategoryResponseDto createdMenu = menuCategoryService.createCategory(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdMenu, "The Menu Category is created successfully"));

    }

    @GetMapping(path = "/categories", version = "1.0")
    public ResponseEntity<ApiResponse<List<MenuCategoryResponseDto>>> getMenuCategories(
            @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(menuCategoryService.getCategories(id)));
    }

    @GetMapping(path = "/categories/{categoryId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDto>> getMenuCategory(
            @PathVariable UUID id, @PathVariable UUID categoryId) {
        return ResponseEntity.ok(ApiResponse.success(menuCategoryService.getCategory(id, categoryId)));
    }

    @PutMapping(path = "/categories/{categoryId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDto>> updateMenuCategory(
            @PathVariable UUID id,
            @PathVariable UUID categoryId,
            @RequestBody MenuCategoryRequestDto request) {
        MenuCategoryResponseDto updated = menuCategoryService.updateCategory(id, categoryId, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "The Menu Category is updated successfully"));
    }

    @DeleteMapping(path = "/categories/{categoryId}", version = "1.0")
    public ResponseEntity<Void> deleteMenuCategory(
            @PathVariable UUID id, @PathVariable UUID categoryId) {
        menuCategoryService.deleteCategory(id, categoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/items", version = "1.0")
    public ResponseEntity<ApiResponse<MenuItemResponseDto>> createMenuItem(
            @PathVariable UUID id,
            @RequestBody MenuItemRequestDto request) {
        MenuItemResponseDto created = menuItemService.createItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "The Menu Item is created successfully"));
    }

    @GetMapping(path = "/items", version = "1.0")
    public ResponseEntity<ApiResponse<List<MenuItemResponseDto>>> getMenuItems(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getItems(id)));
    }

    @GetMapping(path = "/items/{itemId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuItemResponseDto>> getMenuItem(
            @PathVariable UUID id, @PathVariable UUID itemId) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getItem(id, itemId)));
    }

    @PutMapping(path = "/items/{itemId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuItemResponseDto>> updateMenuItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId,
            @RequestBody MenuItemRequestDto request) {
        MenuItemResponseDto updated = menuItemService.updateItem(id, itemId, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "The Menu Item is updated successfully"));
    }

    @DeleteMapping(path = "/items/{itemId}", version = "1.0")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        menuItemService.deleteItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

}
