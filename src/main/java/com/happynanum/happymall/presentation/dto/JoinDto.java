package com.happynanum.happymall.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class JoinDto {

    @NotBlank(message = "아이디를 작성해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9].{5,20}$",message = "아이디는 5~20자의 영어와 숫자의 조합만 가능합니다.")
    private String identifier;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(max = 10, message = "이름은 10자 내로 작성해주세요.")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 5, max = 10, message = "비밀번호는 5~10자로 작성해주세요.")
    private String password;

    @NotBlank(message = "생일을 입력해주세요.")
    private LocalDate birth;

    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$")
    private Integer phoneNumber;

    @NotBlank
    private Integer height;

    @NotBlank
    private Integer weight;

    @NotBlank
    private Integer shoulderLength;

    @NotBlank
    private Integer armLength;

    @NotBlank
    private Integer wishLength;

    @NotBlank
    private Integer legLength;

    @NotBlank
    private String role;

}