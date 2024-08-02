package com.happynanum.happymall.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private Integer age;

    @NotNull
    private Integer phoneNumber;

    @NotNull
    private Integer height;

    @NotNull
    private Integer weight;

    @NotNull
    private Integer shoulderLength;

    @NotNull
    private Integer armLength;

    @NotNull
    private Integer wishLength;

    @NotNull
    private Integer legLength;

    @NotNull
    private String role;

    @Builder.Default
    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    private LocalDateTime modifiedDate = LocalDateTime.now();

}
