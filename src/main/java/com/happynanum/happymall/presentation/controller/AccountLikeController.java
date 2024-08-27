package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.AccountLikeService;
import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class AccountLikeController {

    private final AccountLikeService accountLikeService;

    @PostMapping
    public ResponseEntity<?> addAccountLike(@RequestParam Long productId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        accountLikeService.addAccountLike(accountId, productId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAccountLikes() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        List<ProductResponseDto> products = accountLikeService.getProducts(accountId);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccountLike(@RequestParam Long productId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        accountLikeService.deleteAccountLike(accountId, productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
