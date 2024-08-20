package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.entity.Brand;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.BrandRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private AccountService accountService;

    @AfterEach
    void afterEach() {
        productRepository.deleteAll();
        brandRepository.deleteAll();
    }

    @DisplayName("상품 등록 테스트")
    @Test
    public void productAddTest() {
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto);
        Product product = productRepository.findByName(productRequestDto.getName());
        Brand brand = brandRepository.findByName("테스트 브랜드").get();

        assertThat(product.getDescription()).isEqualTo(productRequestDto.getDescription());
        assertThat(product.getBrand().getName()).isEqualTo(brand.getName());
    }

    @DisplayName("없는 브랜드 넣기 테스트")
    @Test
    public void productAddTestWithNoBrand() {
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .brandName("테스트 브랜드1")
                .name("테스트 상품")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> productService.addProduct(productRequestDto));
    }

    @DisplayName("상품 조회 테스트")
    @Test
    public void productGetTest() {
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품")
                .description("테스트 상품입니다.")
                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto);
        Product product = productRepository.findByName(productRequestDto.getName());
        Long id = product.getId();

        assertThat(productService.getProduct(id).getName()).isEqualTo(productRequestDto.getName());
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