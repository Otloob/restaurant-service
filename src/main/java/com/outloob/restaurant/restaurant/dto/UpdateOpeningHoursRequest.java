package com.outloob.restaurant.restaurant.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Full replacement of a restaurant's opening hours.
 * Sending an empty list clears all configured opening hours.
 */
public record UpdateOpeningHoursRequest(
        @NotNull(message = "openingHours is required")
        List<@Valid OpeningHoursDto> openingHours
) {
}
