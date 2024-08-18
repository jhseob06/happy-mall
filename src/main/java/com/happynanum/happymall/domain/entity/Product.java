package com.happynanum.happymall.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer reviewCount;

    @NotNull
    private Integer saleCount;

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

}
