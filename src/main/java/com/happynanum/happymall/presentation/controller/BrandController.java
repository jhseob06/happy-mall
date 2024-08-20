package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.BrandService;
import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<?> addBrand(@RequestBody @Valid BrandRequestDto brandRequestDto) {
        brandService.addBrand(brandRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
