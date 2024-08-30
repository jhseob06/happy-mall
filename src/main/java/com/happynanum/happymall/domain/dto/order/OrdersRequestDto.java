package com.happynanum.happymall.domain.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class OrdersRequestDto {

    @NotNull(message = "상품 아이디를 입력해주세요")
    private Long productId;

    @NotNull(message = "주소 아이디를 입력해주세요")
    private Long addressId;

    @NotNull(message = "구매하실 수량을 입력해주세요")
    private Integer quantity;

    @NotNull(message = "사이즈를 입력해주세요")
    private String size;

}
