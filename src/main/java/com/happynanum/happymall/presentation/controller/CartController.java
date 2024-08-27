package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.CartService;
import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.dto.cart.CartRequestDto;
import com.happynanum.happymall.domain.dto.cart.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> addCart(@RequestParam Long productId, @RequestParam Integer quantity){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        CartRequestDto cartRequestDto = CartRequestDto.builder()
                .accountId(accountId)
                .productId(productId)
                .quantity(quantity)
                .build();
        cartService.addCart(cartRequestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCart(@PathVariable Long cartId){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        cartService.deleteCart(accountId, cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCarts(){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        List<CartResponseDto> carts = cartService.getCarts(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(carts);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<?> updateCart(@PathVariable Long cartId, @RequestParam Integer quantity){
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        cartService.modifyQuantity(quantity, cartId, accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
