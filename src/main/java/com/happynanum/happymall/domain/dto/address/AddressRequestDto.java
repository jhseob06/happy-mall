package com.happynanum.happymall.domain.dto.address;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class AddressRequestDto {

    @NotBlank(message = "배송지 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "주소를 입력해주세요")
    private String basicAddress;

    @NotBlank(message = "상세 주소를 입력해주세요")
    private String detailedAddress;

    @NotNull(message = "우편번호를 입력해주세요")
    private Integer zoneCode;

}
