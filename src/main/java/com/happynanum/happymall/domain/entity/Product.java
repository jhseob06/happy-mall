package com.happynanum.happymall.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Brand brand;

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    @Column(columnDefinition = "text")
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private Integer quantity;

    @NotNull
    private Integer discount;

    @NotNull
    private Integer reviewCount;

    @NotNull
    private Integer purchaseCount;

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Version
    private Long version;

    public void purchaseProduct(Integer quantity) {
        this.purchaseCount++;
        this.quantity -= quantity;
    }

}
