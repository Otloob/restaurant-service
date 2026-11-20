package com.outloob.restaurant.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Address {

    @Column(name = "street_address", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country", nullable = false)
    private String country;
}
