package com.outloob.restaurant.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OperatingHours {

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    public boolean isOpenAt(LocalTime time) {
        if (openingTime == null || closingTime == null) {
            return true;
        }
        if (closingTime.isAfter(openingTime)) {
            return !time.isBefore(openingTime) && !time.isAfter(closingTime);
        } else {
            return !time.isBefore(openingTime) || !time.isAfter(closingTime);
        }
    }
}