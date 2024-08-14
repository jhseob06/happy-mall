package com.happynanum.happymall.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class AccountResponseDto {

    @NotBlank(message = "아이디를 입력해주세요")
    private String identifier;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birth;

    @NotNull(message = "나이를 입력해주세요")
    private Integer age;

    @NotNull(message = "휴대폰 번호를 입력해주세요")
    private String phoneNumber;

    @NotNull(message = "키를 입력해주세요")
    private Integer height;

    @NotNull(message = "몸무게를 입력해주세요")
    private Integer weight;

    @NotNull(message = "어깨 길이를 입력해주세요")
    private Integer shoulderLength;

    @NotNull(message = "팔 길이를 입력해주세요")
    private Integer armLength;

    @NotNull(message = "허리 길이를 입력해주세요")
    private Integer waistLength;

    @NotNull(message = "다리 길이를 입력해주세요")
    private Integer legLength;

}
