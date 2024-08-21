package com.happynanum.happymall.domain.dto.product;

import com.happynanum.happymall.domain.dto.brand.BrandResponseDto;
import com.happynanum.happymall.domain.entity.Brand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class ProductResponseDto {

    @NotNull
    private Long id;

    @NotNull
    private BrandResponseDto brand;

    @NotBlank
    private String name;

    @NotBlank
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

}
