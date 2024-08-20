package com.happynanum.happymall.domain.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "브랜드 이름을 입력해주세요")
    private String brandName;

    @NotBlank(message = "상품 이름을 입력해주세요")
    @Size(max=40, message = "상품 이름은 40자 내로 작성해주세요")
    private String name;

    @NotBlank(message = "상품에 대한 설명을 입력해주세요")
    @Size(max=600, message = "상품 설명은 600자 내로 작성해주세요")
    private String description;

    @NotNull(message = "가격을 입력해주세요")
    private Integer price;

    @NotNull(message = "할인률을 입력해주세요")
    private Integer discount;

    @NotNull(message = "상품의 재고를 입력해주세요")
    private Integer quantity;

}
