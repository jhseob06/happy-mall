package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.JoinDto;
import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.dto.cart.CartRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.BrandRepository;
import com.happynanum.happymall.domain.repository.CartRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired private CartService cartService;
    @Autowired private ProductService productService;
    @Autowired private AccountService accountService;
    @Autowired private BrandService brandService;
    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private BrandRepository brandRepository;

    @BeforeEach
    void beforeEach() {
        cartRepository.deleteAll();
        productRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @DisplayName("장바구니 추가 테스트")
    @Test
    void addCartTest() {
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
        CartRequestDto cartRequestDto = new CartRequestDto();
        cartRequestDto.setQuantity(10);
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto);
        Account account = accountRepository.findByIdentifier("member");
        Product product = productRepository.findByName("테스트 상품");
        cartRequestDto.setAccountId(account.getId());
        cartRequestDto.setProductId(product.getId());
        cartService.addCart(cartRequestDto);

        assertEquals(1, cartRepository.count());
    }

    @DisplayName("장바구니 삭제 테스트")
    @Test
    void deleteCartTest() {
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
        CartRequestDto cartRequestDto = new CartRequestDto();
        cartRequestDto.setQuantity(10);
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto);
        Account account = accountRepository.findByIdentifier("member");
        Product product = productRepository.findByName("테스트 상품");
        cartRequestDto.setAccountId(account.getId());
        cartRequestDto.setProductId(product.getId());
        cartService.addCart(cartRequestDto);

        cartService.deleteCart(account.getId(), cartRepository.findAll().get(0).getId());

        assertEquals(0, cartRepository.count());
    }

    @DisplayName("장바구니 목록 조회 테스트")
    @Test
    void getCartsTest() {
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
        CartRequestDto cartRequestDto = new CartRequestDto();
        cartRequestDto.setQuantity(10);
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        Account account = accountRepository.findByIdentifier("member");
        Product product1 = productRepository.findByName("테스트 상품1");
        Product product2 = productRepository.findByName("테스트 상품2");
        cartRequestDto.setAccountId(account.getId());
        cartRequestDto.setProductId(product1.getId());
        cartService.addCart(cartRequestDto);
        cartRequestDto.setProductId(product2.getId());
        cartService.addCart(cartRequestDto);

        assertEquals(2, cartService.getCarts(account.getId()).size());
    }

    @DisplayName("장바구니 중복 추가 테스트")
    @Test
    void duplicateCartTest() {
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
        CartRequestDto cartRequestDto = new CartRequestDto();
        cartRequestDto.setQuantity(10);
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto);
        Account account = accountRepository.findByIdentifier("member");
        Product product = productRepository.findByName("테스트 상품");
        cartRequestDto.setAccountId(account.getId());
        cartRequestDto.setProductId(product.getId());
        cartService.addCart(cartRequestDto);

        assertThrows(IllegalArgumentException.class,
                () -> cartService.addCart(cartRequestDto));
    }

    @DisplayName("수량 수정 테스트")
    @Test
    void modifyQuantityTest() {
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
        CartRequestDto cartRequestDto = new CartRequestDto();
        cartRequestDto.setQuantity(10);
        addBrand();

        accountService.joinProcess(joinDto);
        productService.addProduct(productRequestDto);
        Account account = accountRepository.findByIdentifier("member");
        Product product = productRepository.findByName("테스트 상품");
        cartRequestDto.setAccountId(account.getId());
        cartRequestDto.setProductId(product.getId());
        cartService.addCart(cartRequestDto);

        cartService.modifyQuantity(20, cartRepository.findAll().get(0).getId(), account.getId());

        assertEquals(20, cartRepository.findAll().get(0).getQuantity());
    }

    void addBrand() {
        if(brandRepository.findByName("테스트 브랜드").isPresent()) return;
        BrandRequestDto brand = BrandRequestDto.builder()
                .name("테스트 브랜드")
                .description("테스트 브랜드입니다.")
                .phoneNumber("01012341234")
                .build();
        brandService.addBrand(brand);
    }

}