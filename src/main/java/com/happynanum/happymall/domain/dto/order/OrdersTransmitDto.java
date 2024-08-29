package com.happynanum.happymall.domain.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class OrdersTransmitDto {

    @NotBlank(message = "apiKey를 입력해주세요")
    private String apiKey;

    @NotNull(message = "orderNo를 입력해주세요")
    private String orderNo;

    private String productDesc;

    private String retUrl;

    private String retCancelUrl;

    private Integer amount;

    private Integer amountTaxFree;

    private String payToken;

}
