package com.happynanum.happymall.domain.dto.cart;

import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class CartResponseDto {

    @NotNull(message = "장바구니 식별자를 입력해주세요")
    private Long id;

    @NotNull(message = "상품 아이디를 입력해주세요")
    private ProductResponseDto product;

    @NotNull(message = "구매하실 수량을 입력해주세요")
    private Integer quantity;

}
