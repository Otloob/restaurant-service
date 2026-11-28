package com.outloob.restaurant.menu.entity;
import com.outloob.restaurant.common.BaseEntity;
import com.outloob.restaurant.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "menu_category", uniqueConstraints =
        @UniqueConstraint(name = "uk_menu_category_restaurant_name", columnNames = {"restaurant_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToMany(mappedBy = "category")
    private List<MenuItem> items;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="description")
    private String description;

}
