package com.happynanum.happymall.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String identifier;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private LocalDate birth;

    @NotBlank
    private Integer age;

    @NotBlank
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

    @Builder.Default
    @NotBlank
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotBlank
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

}
