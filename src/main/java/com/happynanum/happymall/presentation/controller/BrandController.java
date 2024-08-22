package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.BrandService;
import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<?> addBrand(@RequestBody @Valid BrandRequestDto brandRequestDto) {
        brandService.addBrand(brandRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyBrand(@PathVariable Long id, @RequestBody @Valid BrandRequestDto brandRequestDto) {
        brandService.modifyBrand(id, brandRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
