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

    @NotNull
    private LocalDate birth;

    @NotNull
    private Integer age;

    @NotNull
    private String phoneNumber;

    @NotNull
    private Integer height;

    @NotNull
    private Integer weight;

    @NotNull
    private Integer shoulderLength;

    @NotNull
    private Integer armLength;

    @NotNull
    private Integer waistLength;

    @NotNull
    private Integer legLength;

    @NotBlank
    private String role;

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

}
