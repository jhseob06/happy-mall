package com.happynanum.happymall.domain.dto.account;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class AccountPatchRequestDto {

    @Nullable
    @Size(min = 5, max = 10, message = "비밀번호는 5~10자로 작성해주세요")
    private String newPassword;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String value;

}
