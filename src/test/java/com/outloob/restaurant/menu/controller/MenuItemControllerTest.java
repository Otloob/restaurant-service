package com.outloob.restaurant.menu.controller;

import com.outloob.restaurant.config.WebConfig;
import com.outloob.restaurant.common.ApiResponse;
import com.outloob.restaurant.menu.dto.MenuItemRequestDto;
import com.outloob.restaurant.menu.dto.MenuItemResponseDto;
import com.outloob.restaurant.menu.service.MenuCategoryService;
import com.outloob.restaurant.menu.service.MenuItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@Import(WebConfig.class)
class MenuItemControllerTest {

    private static final String API_VERSION = "application/vnd.jobra+json;v=1.0";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuCategoryService menuCategoryService;

    @MockitoBean
    private MenuItemService menuItemService;

    // The app globally enables JPA auditing; an MVC slice has no JPA metamodel.
    @MockitoBean(name = "jpaMappingContext")
    private JpaMetamodelMappingContext jpaMappingContext;

    @Test
    void createsItemAndReturnsCreated() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(menuItemService.createItem(restaurantId,
                new MenuItemRequestDto(categoryId, "Latte", "Milk coffee", 4.5,
                        "https://example.com/latte.jpg", true)))
                .thenReturn(new MenuItemResponseDto(itemId, categoryId, "Latte", "Milk coffee", 4.5,
                        "https://example.com/latte.jpg", true));

        mockMvc.perform(post("/api/merchant/restaurants/{id}/items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(API_VERSION)
                        .content(itemJson(categoryId, "Latte", "Milk coffee", 4.5,
                                "https://example.com/latte.jpg", true)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(itemId.toString()))
                .andExpect(jsonPath("$.data.categoryId").value(categoryId.toString()))
                .andExpect(jsonPath("$.data.name").value("Latte"));
    }

    @Test
    void listsItemsForRestaurant() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        when(menuItemService.getItems(restaurantId, null, 0, 20)).thenReturn(
                new ApiResponse.PagedData<>(List.of(
                        new MenuItemResponseDto(UUID.randomUUID(), categoryId, "Latte", "Milk coffee", 4.5,
                                null, true)),
                        new ApiResponse.PageMetadata(0, 20, 1, 1, true)));

        mockMvc.perform(get("/api/merchant/restaurants/{id}/items", restaurantId)
                        .accept(API_VERSION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Latte"));
    }

    @Test
    void getsItemById() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(menuItemService.getItem(restaurantId, itemId))
                .thenReturn(new MenuItemResponseDto(itemId, categoryId, "Latte", "Milk coffee", 4.5,
                        null, true));

        mockMvc.perform(get("/api/merchant/restaurants/{id}/items/{itemId}", restaurantId, itemId)
                        .accept(API_VERSION))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(itemId.toString()));
    }

    @Test
    void updatesItem() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        when(menuItemService.updateItem(restaurantId, itemId,
                new MenuItemRequestDto(categoryId, "Iced Latte", "Cold milk coffee", 5.0,
                        null, true)))
                .thenReturn(new MenuItemResponseDto(itemId, categoryId, "Iced Latte", "Cold milk coffee", 5.0,
                        null, true));

        mockMvc.perform(put("/api/merchant/restaurants/{id}/items/{itemId}", restaurantId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(API_VERSION)
                        .content(itemJson(categoryId, "Iced Latte", "Cold milk coffee", 5.0, null, true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Iced Latte"))
                .andExpect(jsonPath("$.data.price").value(5.0));
    }

    @Test
    void deletesItemAndReturnsNoContent() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        mockMvc.perform(delete("/api/merchant/restaurants/{id}/items/{itemId}", restaurantId, itemId)
                        .accept(API_VERSION))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(menuItemService).deleteItem(restaurantId, itemId);
    }

    @Test
    void returnsNotFoundWhenItemDoesNotBelongToRestaurant() throws Exception {
        UUID restaurantId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu item not found"))
                .when(menuItemService).deleteItem(restaurantId, itemId);

        mockMvc.perform(delete("/api/merchant/restaurants/{id}/items/{itemId}", restaurantId, itemId)
                        .accept(API_VERSION))
                .andExpect(status().isNotFound());
    }

    private String itemJson(UUID categoryId, String name, String description, double price,
                            String imageUrl, boolean available) {
        String imageValue = imageUrl == null ? "null" : "\"" + imageUrl + "\"";
        return "{\"categoryId\":\"" + categoryId + "\",\"name\":\"" + name
                + "\",\"description\":\"" + description + "\",\"price\":" + price
                + ",\"imageUrl\":" + imageValue + ",\"available\":" + available + "}";
    }
}
