package com.outloob.restaurant.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Address {

    @NotBlank
    @Size(max = 255)
    @Column(name = "street_address", nullable = false)
    private String street;

    @NotBlank
    @Size(max = 255)
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 255)
    @Column(name = "state")
    private String state;

    @NotBlank
    @Size(max = 255)
    @Column(name = "country", nullable = false)
    private String country;
}
