package com.happynanum.happymall.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    private Integer quantity;

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

}