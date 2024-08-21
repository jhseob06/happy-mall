package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.ProductService;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        ProductResponseDto product = productService.getProduct(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        productService.addProduct(productRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
