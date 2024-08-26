package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.JoinDto;
import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.repository.AccountLikeRepository;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.BrandRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountLikeServiceTest {

    @Autowired private AccountLikeService accountLikeService;
    @Autowired private AccountService accountService;
    @Autowired private ProductService productService;
    @Autowired private BrandService brandService;
    @Autowired private AccountRepository accountRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AccountLikeRepository accountLikeRepository;
    @Autowired private BrandRepository brandRepository;

    @BeforeEach
    void beforeEach() {
        accountLikeRepository.deleteAll();
        accountRepository.deleteAll();
        productRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @DisplayName("사용자 찜 추가 테스트")
    @Test
    void addAccountLikeTest() {
        JoinDto joinDto = JoinDto.builder()
                .identifier("member")
                .name("member")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .waistLength(60)
                .legLength(120)
                .build();
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        Long productId = productRepository.findByName("테스트 상품").getId();
        accountLikeService.addAccountLike(accountId, productId);

        accountLikeRepository.findProductsByAccountId(accountId).forEach(product -> {
            assertEquals(product.getId(), productId);
        });
    }

    @DisplayName("사용자 찜 삭제 테스트")
    @Test
    void deleteAccountLikeTest() {
        JoinDto joinDto = JoinDto.builder()
                .identifier("member")
                .name("member")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .waistLength(60)
                .legLength(120)
                .build();
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        Long productId = productRepository.findByName("테스트 상품").getId();
        accountLikeService.addAccountLike(accountId, productId);

        accountLikeRepository.findProductsByAccountId(accountId).forEach(product -> {
            assertEquals(product.getId(), productId);
        });
        accountLikeService.deleteAccountLike(accountId, productId);

        assertThat(accountLikeRepository.count()).isEqualTo(0);
    }

    @DisplayName("사용자 찜 상품 조회 테스트")
    @Test
    void getAccountLikeProductsTest() {
        JoinDto joinDto = JoinDto.builder()
                .identifier("member")
                .name("member")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .waistLength(60)
                .legLength(120)
                .build();
        ProductRequestDto productRequestDto1 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품1")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto2 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품2")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        Long productId1 = productRepository.findByName("테스트 상품1").getId();
        Long productId2 = productRepository.findByName("테스트 상품2").getId();
        accountLikeService.addAccountLike(accountId, productId1);
        accountLikeService.addAccountLike(accountId, productId2);

        accountLikeService.getProducts(accountId).forEach(product -> {
            assertThat(product.getId()).isIn(productId1, productId2);
        });
    }

    void addBrand() {
        BrandRequestDto brand = BrandRequestDto.builder()
                .name("테스트 브랜드")
                .description("테스트 브랜드입니다.")
                .phoneNumber("01012341234")
                .build();
        brandService.addBrand(brand);
    }
}