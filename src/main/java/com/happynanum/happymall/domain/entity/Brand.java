package com.happynanum.happymall.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Lob
    @Column(columnDefinition = "text")
    private String description;

    @NotNull
    private Integer productCount;

    @NotBlank
    private String phoneNumber;

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

}
