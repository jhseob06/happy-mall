package com.happynanum.happymall.domain.dto.cart;

import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class CartRequestDto {

    @NotNull(message = "회원 아이디를 입력해주세요")
    private Long accountId;

    @NotNull(message = "상품 아이디를 입력해주세요")
    private Long productId;

    @NotNull(message = "구매하실 수량을 입력해주세요")
    private Integer quantity;
    
}
