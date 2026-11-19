package com.outloob.restaurant.restaurant.dto;

import java.io.Serializable;
import java.time.LocalTime;

public record OperatingHoursDto(LocalTime openingTime, LocalTime closingTime) implements Serializable {
}