package com.happynanum.happymall.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @NotBlank(message = "아이디를 입력해주세요")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$",message = "아이디는 5~20자의 영어와 숫자의 조합만 가능합니다")
    private String identifier;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(max = 10, message = "이름은 10자 내로 작성해주세요")
    private String name;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 5, max = 10, message = "비밀번호는 5~10자로 작성해주세요")
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @NotNull(message = "생일을 입력해주세요")
    private LocalDate birth;

    @NotNull(message = "휴대폰 번호를 입력해주세요")
    @Pattern(regexp = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$")
    private String phoneNumber;

//    @NotNull(message = "키를 입력해주세요")
    private Integer height;

//    @NotNull(message = "몸무게를 입력해주세요")
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
