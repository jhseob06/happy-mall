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
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Account account;

    @NotBlank
    @Lob
    private String description;

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

}
