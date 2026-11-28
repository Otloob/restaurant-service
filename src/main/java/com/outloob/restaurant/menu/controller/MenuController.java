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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.outloob.restaurant.common.util.RequestValueUtils.pathUuid;
import static com.outloob.restaurant.common.util.RequestValueUtils.requestInt;
import static com.outloob.restaurant.common.util.RequestValueUtils.requestUuid;

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
            @PathVariable Map<String, String> pathVariables,
            @RequestBody MenuCategoryRequestDto request) {
        MenuCategoryResponseDto createdMenu = menuCategoryService.createCategory(
                pathUuid(pathVariables, "id"), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(createdMenu, "The Menu Category is created successfully"));

    }

    @GetMapping(path = "/categories", version = "1.0")
    public ResponseEntity<ApiResponse<ApiResponse.PagedData<MenuCategoryResponseDto>>> getMenuCategories(
            @PathVariable Map<String, String> pathVariables,
            @RequestParam Map<String, String> requestParams) {
        return ResponseEntity.ok(ApiResponse.success(
                menuCategoryService.getCategories(pathUuid(pathVariables, "id"),
                        requestInt(requestParams, "page", 0), requestInt(requestParams, "limit", 20))));
    }

    @GetMapping(path = "/categories/{categoryId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDto>> getMenuCategory(
            @PathVariable Map<String, String> pathVariables) {
        return ResponseEntity.ok(ApiResponse.success(menuCategoryService.getCategory(
                pathUuid(pathVariables, "id"), pathUuid(pathVariables, "categoryId"))));
    }

    @PutMapping(path = "/categories/{categoryId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuCategoryResponseDto>> updateMenuCategory(
            @PathVariable Map<String, String> pathVariables,
            @RequestBody MenuCategoryRequestDto request) {
        MenuCategoryResponseDto updated = menuCategoryService.updateCategory(
                pathUuid(pathVariables, "id"), pathUuid(pathVariables, "categoryId"), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "The Menu Category is updated successfully"));
    }

    @DeleteMapping(path = "/categories/{categoryId}", version = "1.0")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable Map<String, String> pathVariables) {
        menuCategoryService.deleteCategory(
                pathUuid(pathVariables, "id"), pathUuid(pathVariables, "categoryId"));
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/items", version = "1.0")
    public ResponseEntity<ApiResponse<MenuItemResponseDto>> createMenuItem(
            @RequestBody MenuItemRequestDto request,
            @PathVariable Map<String, String> pathVariables) {
        MenuItemResponseDto created = menuItemService.createItem(pathUuid(pathVariables, "id"), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "The Menu Item is created successfully"));
    }

    @GetMapping(path = "/items", version = "1.0")
    public ResponseEntity<ApiResponse<ApiResponse.PagedData<MenuItemResponseDto>>> getMenuItems(
            @PathVariable Map<String, String> pathVariables,
            @RequestParam Map<String, String> requestParams) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getItems(
                pathUuid(pathVariables, "id"), requestUuid(requestParams, "categoryId"),
                requestInt(requestParams, "page", 0), requestInt(requestParams, "limit", 20))));
    }

    @GetMapping(path = "/items/{itemId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuItemResponseDto>> getMenuItem(
            @PathVariable Map<String, String> pathVariables) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getItem(
                pathUuid(pathVariables, "id"), pathUuid(pathVariables, "itemId"))));
    }

    @PutMapping(path = "/items/{itemId}", version = "1.0")
    public ResponseEntity<ApiResponse<MenuItemResponseDto>> updateMenuItem(
            @PathVariable Map<String, String> pathVariables,
            @RequestBody MenuItemRequestDto request) {
        MenuItemResponseDto updated = menuItemService.updateItem(
                pathUuid(pathVariables, "id"), pathUuid(pathVariables, "itemId"), request);
        return ResponseEntity.ok(ApiResponse.success(updated, "The Menu Item is updated successfully"));
    }

    @DeleteMapping(path = "/items/{itemId}", version = "1.0")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Map<String, String> pathVariables) {
        menuItemService.deleteItem(
                pathUuid(pathVariables, "id"), pathUuid(pathVariables, "itemId"));
        return ResponseEntity.noContent().build();
    }

}
