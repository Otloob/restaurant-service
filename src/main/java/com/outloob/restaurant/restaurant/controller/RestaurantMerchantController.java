package com.outloob.restaurant.restaurant.controller;

import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.dto.RestaurantResponseDto;
import com.outloob.restaurant.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchants/restaurants")
@AllArgsConstructor
public class RestaurantMerchantController {

    @Autowired
    final RestaurantService restaurantService;

    @PostMapping(version = "1.0")
    public ResponseEntity<ApiResponse<RestaurantResponseDto>> createRestaurant(
            @Valid @RequestBody RestaurantRequestDto restaurant) {
        RestaurantResponseDto createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok()
                .body(ApiResponse.success(createdRestaurant , "Restaurant registered successfully and is pending admin approval"));
    }

}
