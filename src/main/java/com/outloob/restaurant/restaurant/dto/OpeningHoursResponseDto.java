package com.outloob.restaurant.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;
import java.util.UUID;

public record OpeningHoursResponseDto(
        UUID id,
        int dayOfWeek,
        @JsonFormat(pattern = "HH:mm") LocalTime opensAt,
        @JsonFormat(pattern = "HH:mm") LocalTime closesAt
) {}
