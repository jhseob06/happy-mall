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

    @NotNull(message = "상품 식별자를 입력해주세요")
    private Long id;

    @NotNull(message = "브랜드를 입력해주세요")
    private BrandResponseDto brand;

    @NotBlank(message = "상품 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "상품 설명을 입력해주세요")
    private String description;

    @NotNull(message = "상품 가격을 입력해주세요")
    private Integer price;

    @NotNull(message = "상품 수량을 입력해주세요")
    private Integer quantity;

    @NotNull(message = "상품 할인율을 입력해주세요")
    private Integer discount;

    @NotNull(message = "상품 리뷰 수를 입력해주세요")
    private Integer reviewCount;

    @NotNull(message = "상품 구매 수를 입력해주세요")
    private Integer purchaseCount;

}
