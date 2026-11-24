package com.outloob.restaurant.menu.controller;

import com.outloob.restaurant.config.WebConfig;
import com.outloob.restaurant.menu.dto.MenuCategoryRequestDto;
import com.outloob.restaurant.menu.dto.MenuCategoryResponseDto;
import com.outloob.restaurant.menu.service.MenuCategoryService;
import com.outloob.restaurant.menu.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@Import(WebConfig.class)
class MenuControllerTest {

    private static final String API_VERSION = "application/vnd.jobra+json;v=1.0";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuCategoryService menuCategoryService;

    @MockitoBean
    private MenuItemService menuItemService;

    // The application enables JPA auditing globally; the MVC slice has no JPA metamodel.
    @MockitoBean(name = "jpaMappingContext")
    private JpaMetamodelMappingContext jpaMappingContext;

    @Test
    void createsCategoryAndReturnsCreated() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        when(menuCategoryService.createCategory(restaurantId,
                new MenuCategoryRequestDto("Drinks", "Cold drinks")))
                .thenReturn(new MenuCategoryResponseDto(categoryId, "Drinks", "Cold drinks"));

        mockMvc.perform(post("/api/merchant/restaurants/{id}/categories", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(API_VERSION)
                        .content("{\"name\":\"Drinks\",\"description\":\"Cold drinks\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.data.name").value("Drinks"));
    }

    @Test
    void listsCategoriesForRestaurant() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        when(menuCategoryService.getCategories(restaurantId)).thenReturn(List.of(
                new MenuCategoryResponseDto(UUID.randomUUID(), "Drinks", "Cold drinks")));

        mockMvc.perform(get("/api/merchant/restaurants/{id}/categories", restaurantId)
                        .accept(API_VERSION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("Drinks"));
    }

    @Test
    void getsCategoryById() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        when(menuCategoryService.getCategory(restaurantId, categoryId))
                .thenReturn(new MenuCategoryResponseDto(categoryId, "Drinks", "Cold drinks"));

        mockMvc.perform(get("/api/merchant/restaurants/{id}/categories/{categoryId}",
                        restaurantId, categoryId).accept(API_VERSION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(categoryId.toString()));
    }

    @Test
    void updatesCategory() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        when(menuCategoryService.updateCategory(restaurantId, categoryId,
                new MenuCategoryRequestDto("Hot Drinks", "Coffee and tea")))
                .thenReturn(new MenuCategoryResponseDto(categoryId, "Hot Drinks", "Coffee and tea"));

        mockMvc.perform(put("/api/merchant/restaurants/{id}/categories/{categoryId}",
                        restaurantId, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(API_VERSION)
                        .content("{\"name\":\"Hot Drinks\",\"description\":\"Coffee and tea\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Hot Drinks"));
    }

    @Test
    void deletesCategoryAndReturnsNoContent() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        mockMvc.perform(delete("/api/merchant/restaurants/{id}/categories/{categoryId}",
                        restaurantId, categoryId).accept(API_VERSION))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(menuCategoryService).deleteCategory(restaurantId, categoryId);
    }

    @Test
    void returnsNotFoundWhenServiceCannotFindCategory() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu category not found"))
                .when(menuCategoryService).deleteCategory(restaurantId, categoryId);

        mockMvc.perform(delete("/api/merchant/restaurants/{id}/categories/{categoryId}",
                        restaurantId, categoryId).accept(API_VERSION))
                .andExpect(status().isNotFound());
    }
}
