package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandResponseDto;
import com.happynanum.happymall.domain.dto.cart.CartRequestDto;
import com.happynanum.happymall.domain.dto.cart.CartResponseDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Cart;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.CartRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    
   private final CartRepository cartRepository;
   private final AccountRepository accountRepository;
   private final ProductRepository productRepository;

        @Transactional
        public void addCart(CartRequestDto cartRequestDto) {
            Long accountId = cartRequestDto.getAccountId();
            Long productId = cartRequestDto.getProductId();
            Integer quantity = cartRequestDto.getQuantity();

            Account account = accountRepository.findById(accountId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));
            Product product = productRepository.findById(productId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 상품 식별자입니다 = " + productId));

            duplicateCartCheck(account, product);

            Cart cart = Cart.builder()
                    .account(account)
                    .product(product)
                    .quantity(quantity)
                    .build();

            cartRepository.save(cart);
            log.info("장바구니 추가 완료 = {}(사용자 식별자), {}(상품 식별자)", accountId, productId);
        }

        @Transactional
        public void deleteCart(Long accountId, Long cartId){

            Account account = accountRepository.findById(accountId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));

            Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 장바구니 식별자입니다 = " + cartId));

            if(!cart.getAccount().equals(account)) {
                throw new IllegalArgumentException("해당 장바구니는 사용자의 장바구니가 아닙니다 = " + cartId);
            }

            cartRepository.delete(cart);
            log.info("장바구니 삭제 완료 = {}(사용자 식별자), {}(장바구니 식별자)", accountId, cartId);
        }


        @Transactional
        public List<CartResponseDto> getCarts(Long accountId) {
            Account account = accountRepository.findById(accountId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));

            List<Cart> carts = cartRepository.findCartsByAccount(account);

            List<CartResponseDto> cartResponseDtos =
                    carts.stream().map(this::cartToCartResponseDto).toList();

            log.info("장바구니 목록 조회 완료 = {}", accountId);
            return cartResponseDtos;
        }

        @Transactional
        public void modifyQuantity(Integer quantity, Long cartId, Long accountId) {
            Account account = accountRepository.findById(accountId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));
            Cart cart = cartRepository.findById(cartId).orElseThrow(() ->
                    new IllegalArgumentException("존재하지 않는 장바구니 식별자입니다 = " + cartId));

            if(!cart.getAccount().equals(account)) {
                throw new IllegalArgumentException("해당 장바구니는 사용자의 장바구니가 아닙니다 = " + cartId);
            }

            cart.updateQuantity(quantity);
            cartRepository.save(cart);

            log.info("장바구니 수량 수정 완료 = {}(장바구니 식별자) {}(수량)", cartId, quantity);
        }

        private CartResponseDto cartToCartResponseDto(Cart cart) {
            return CartResponseDto.builder()
                    .id(cart.getId())
                    .product(
                            ProductResponseDto.builder()
                                    .id(cart.getProduct().getId())
                                    .brand(
                                            BrandResponseDto.builder()
                                                    .name(cart.getProduct().getBrand().getName())
                                                    .description(cart.getProduct().getBrand().getDescription())
                                                    .phoneNumber(cart.getProduct().getBrand().getPhoneNumber())
                                                    .build()
                                    )
                                    .name(cart.getProduct().getName())
                                    .description(cart.getProduct().getDescription())
                                    .price(cart.getProduct().getPrice())
                                    .quantity(cart.getProduct().getQuantity())
                                    .reviewCount(cart.getProduct().getReviewCount())
                                    .purchaseCount(cart.getProduct().getPurchaseCount())
                                    .discount(cart.getProduct().getDiscount())
                                    .build()
                    )
                    .quantity(cart.getQuantity())
                    .build();
        }

        private void duplicateCartCheck(Account account, Product product) {
            if (cartRepository.existsByAccountAndProduct(account, product)) {
                throw new IllegalArgumentException("이미 장바구니에 담긴 상품입니다 = " + product.getName());
            }
        }
    
}
