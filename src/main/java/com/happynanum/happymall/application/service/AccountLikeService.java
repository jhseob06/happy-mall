package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandResponseDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.AccountLike;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.AccountLikeRepository;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLikeService {

    private final AccountLikeRepository accountLikeRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addAccountLike(Long accountId, Long productId) {
         Account account = accountRepository.findById(accountId).orElseThrow(() ->
                 new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));
         Product product = productRepository.findById(productId).orElseThrow(() ->
                 new IllegalArgumentException("존재하지 않는 상품 식별자입니다 = " + productId));

         duplicateAccountLikeCheck(account, product);

         AccountLike accountLike = AccountLike.builder()
                .account(account)
                .product(product)
                .build();

        accountLikeRepository.save(accountLike);
        log.info("사용자 찜 추가 완료 = {}(사용자 식별자) {}(상품 식별자)",accountId, productId);
    }

    @Transactional
    public List<ProductResponseDto> getProducts(Long accountId) {
        List<Product> products = accountLikeRepository.findProductsByAccountId(accountId);

        List<ProductResponseDto> productResponseDtos = products.stream()
                .map(this::productToProductResponseDto)
                .toList();

        log.info("사용자 찜 목록 조회 완료 = {}", accountId);
        return productResponseDtos;
    }

    @Transactional
    public void deleteAccountLike(Long accountId, Long productId) {
        AccountLike accountLike = accountLikeRepository.findByAccountIdAndProductId(accountId, productId).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 사용자 찜입니다 = " + accountId + "(회원아이디) " + productId + "(상품아이디)"));
        accountLikeRepository.delete(accountLike);
        log.info("사용자 찜 삭제 완료 = {}", accountLike.getId());
    }

    private void duplicateAccountLikeCheck(Account account, Product product) {
        if (accountLikeRepository.existsByAccountAndProduct(account, product)) {
            throw new IllegalArgumentException("이미 찜한 상품입니다 = " + product.getName());
        }
    }

    private ProductResponseDto productToProductResponseDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .brand(
                        BrandResponseDto.builder()
                                .name(product.getBrand().getName())
                                .description(product.getBrand().getDescription())
                                .phoneNumber(product.getBrand().getPhoneNumber())
                                .build()
                )
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .reviewCount(product.getReviewCount())
                .purchaseCount(product.getPurchaseCount())
                .discount(product.getDiscount())
                .build();
    }

}
