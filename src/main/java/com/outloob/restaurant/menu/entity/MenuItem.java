package com.outloob.restaurant.menu.entity;


import com.outloob.restaurant.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="menu_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;

    @Column(name = "name" , nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name ="price")
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name="available")
    private Boolean available;

    @Column(name="tag")
    private String tag;

}
