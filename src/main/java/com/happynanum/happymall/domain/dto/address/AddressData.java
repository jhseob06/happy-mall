package com.happynanum.happymall.domain.dto.address;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class AddressData {

    @NotNull(message = "주소를 입력해주세요")
    private String basicAddress;

    @NotNull(message = "상세 주소를 입력해주세요")
    private String detailedAddress;

    @NotNull(message = "우편번호를 입력해주세요")
    private Integer zoneCode;

}
