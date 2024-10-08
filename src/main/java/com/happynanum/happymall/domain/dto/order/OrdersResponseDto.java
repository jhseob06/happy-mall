package com.happynanum.happymall.domain.dto.order;

import com.happynanum.happymall.domain.dto.address.AddressData;
import com.happynanum.happymall.domain.dto.address.AddressResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class OrdersResponseDto {

    @NotNull(message = "주문 식별자를 입력해주세요")
    private Long id;

    @NotNull(message = "주소를 입력해주세요")
    private AddressData address;

    @NotNull(message = "상품 식별자를 입력해주세요")
    private Long productId;

    @NotBlank(message = "주문 번호를 입력해주세요")
    private String orderNo;

    @NotBlank(message = "상품 이름을 입력해주세요")
    private String productDesc;

    @NotNull(message = "상품 수량을 입력해주세요")
    private Integer quantity;

    @NotBlank(message = "배송 상태를 입력해주세요")
    private String deliveryStatus;

    @NotBlank(message = "사이즈를 입력해주세요")
    private String size;

    @NotNull(message = "결제 금액을 입력해주세요")
    private Integer amount;

    @NotNull(message = "할인 금액을 입력해주세요")
    private Integer discountAmount;

    @NotBlank(message = "결제 방법을 입력해주세요")
    private String payMethod;

}
