package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody String name){
        categoryService.addCategory(name);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
