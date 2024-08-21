package com.happynanum.happymall.domain.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class BrandResponseDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String phoneNumber;

}
