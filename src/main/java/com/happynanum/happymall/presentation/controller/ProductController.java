package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.ProductCategoryService;
import com.happynanum.happymall.application.service.ProductService;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

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

    @PostMapping("/{id}")
    public ResponseEntity<?> addProductCategory(@PathVariable Long id, @RequestParam Long categoryId) {
        productCategoryService.addProductCategory(id, categoryId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getProducts(@RequestParam List<Long> categoryIds, @RequestParam int page) {
        Page<ProductResponseDto> products = productCategoryService.getProducts(categoryIds, page);
        return ResponseEntity.ok().body(products);
    }
}
