package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.common.exception.InvalidRequestException;
import com.outloob.restaurant.common.exception.ResourceNotFoundException;
import com.outloob.restaurant.restaurant.dto.RestaurantPublicDetailsDto;
import com.outloob.restaurant.restaurant.entity.Address;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.enums.RestaurantStatus;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class RestaurantPublicServiceImplTests {

    private final RestaurantRepository repository = mock(RestaurantRepository.class);
    private final RestaurantPublicServiceImpl service = new RestaurantPublicServiceImpl(repository);

    @Test
    void listingRejectsInvalidPaginationBeforeQueryingRepository() {
        assertThatThrownBy(() -> service.getRestaurants(-1, 20))
                .isInstanceOf(InvalidRequestException.class);
        assertThatThrownBy(() -> service.getRestaurants(0, 0))
                .isInstanceOf(InvalidRequestException.class);
        assertThatThrownBy(() -> service.getRestaurants(0, 101))
                .isInstanceOf(InvalidRequestException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void listingOnlyQueriesActiveRestaurants() {
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Pizza Place")
                .address(new Address("Main Street", "Cairo", null, "Egypt"))
                .build();
        when(repository.findAllByStatus(eq(RestaurantStatus.ACTIVE), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(restaurant)));

        var page = service.getRestaurants(0, 20);

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().name()).isEqualTo("Pizza Place");
    }

    @Test
    void detailsReturnsActiveRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .id(UUID.randomUUID())
                .name("Pizza Place")
                .phoneNumber("1234567")
                .address(new Address("Main Street", "Cairo", null, "Egypt"))
                .build();
        when(repository.findByIdAndStatus(restaurant.getId(), RestaurantStatus.ACTIVE))
                .thenReturn(Optional.of(restaurant));

        RestaurantPublicDetailsDto response = service.getRestaurantDetails(restaurant.getId());

        assertThat(response.id()).isEqualTo(restaurant.getId());
        assertThat(response.phoneNumber()).isEqualTo("1234567");
        assertThat(response.openingHours()).isEmpty();
    }

    @Test
    void detailsTreatsNonActiveRestaurantAsNotFound() {
        UUID restaurantId = UUID.randomUUID();
        when(repository.findByIdAndStatus(restaurantId, RestaurantStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getRestaurantDetails(restaurantId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
