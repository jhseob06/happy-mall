package com.happynanum.happymall.domain.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class BrandRequestDto {

    @NotBlank(message = "브랜드 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "브랜드에 대한 설명을 입력해주세요")
    @Size(max=800, message = "브랜드 설명은 800자 내로 작성해주세요")
    private String description;

    @NotBlank(message = "브랜드의 전화번호를 입력해주세요")
    private String phoneNumber;

}
