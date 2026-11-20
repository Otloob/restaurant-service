package com.outloob.restaurant.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record OpeningHoursDto(
        @Min(1) @Max(7)
        int dayOfWeek, // 1 = Monday, ..., 7 = Sunday

        @NotNull(message = "Opening time is required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime opensAt,

        @NotNull(message = "Closing time is required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime closesAt
) {}
