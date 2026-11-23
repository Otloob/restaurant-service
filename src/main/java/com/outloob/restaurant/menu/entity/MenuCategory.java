package com.outloob.restaurant.menu.entity;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Entity
public class MenuCategory {

    @Id
    private UUID id;

    @OneToMany(mappedBy = "category")
    private List<MenuItem> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name="name", unique=true,nullable=false)
    private String name;

}
