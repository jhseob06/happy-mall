package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<?> addProductCategory(@RequestParam Long productId, @RequestParam Long categoryId) {
        productCategoryService.addProductCategory(productId, categoryId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductCategory(@PathVariable Long id) {
        productCategoryService.deleteProductCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
