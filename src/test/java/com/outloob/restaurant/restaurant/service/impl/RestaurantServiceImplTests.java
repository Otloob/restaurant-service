package com.outloob.restaurant.restaurant.service.impl;

import com.outloob.restaurant.common.exception.InvalidRequestException;
import com.outloob.restaurant.restaurant.dto.RestaurantRequestDto;
import com.outloob.restaurant.restaurant.entity.Address;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import com.outloob.restaurant.restaurant.repository.RestaurantRepository;
import com.outloob.restaurant.restaurant.service.RestaurantAccessService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class RestaurantServiceImplTests {

    private final RestaurantRepository repository = mock(RestaurantRepository.class);
    private final RestaurantServiceImpl service =
            new RestaurantServiceImpl(repository, mock(RestaurantAccessService.class));
    private final RestaurantRequestDto request = new RestaurantRequestDto(
            "Restaurant", null, "restaurant@example.com", "1234567",
            0, 0, 100, new Address("Main Street", "City", null, "Country"));

    @Test
    void createRejectsMissingOwnerBeforeAccessingRepository() {
        assertThatThrownBy(() -> service.createRestaurant(request, null))
                .isInstanceOf(InvalidRequestException.class);
        verifyNoInteractions(repository);
    }

    @Test
    void createSavesAndReturnsSuppliedOwner() {
        UUID ownerId = UUID.randomUUID();
        when(repository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.createRestaurant(request, ownerId);

        var saved = ArgumentCaptor.forClass(Restaurant.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getOwnerId()).isEqualTo(ownerId);
        assertThat(response.ownerId()).isEqualTo(ownerId);
    }
}
