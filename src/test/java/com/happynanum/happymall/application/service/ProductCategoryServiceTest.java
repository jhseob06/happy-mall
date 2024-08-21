package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Brand;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.CategoryRepository;
import com.happynanum.happymall.domain.repository.ProductCategoryRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductCategoryServiceTest {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired private ProductService productService;

    @Autowired private CategoryService categoryService;

    @Autowired private BrandService brandService;

    @Autowired private CategoryRepository categoryRepository;

    @Autowired private ProductRepository productRepository;

    @Autowired private ProductCategoryRepository productCategoryRepository;

    @AfterEach
    void afterEach() {
        productCategoryRepository.deleteAll();
        categoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @DisplayName("상품 카테고리 테스트")
    @Test
    void addProductCategory() {
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
        String categoryName1 = "테스트 카테고리1";
        String categoryName2 = "테스트 카테고리2";

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        categoryService.addCategory(categoryName1);
        categoryService.addCategory(categoryName2);
        Long productId1 = productRepository.findByName("테스트 상품1").getId();
        Long productId2 = productRepository.findByName("테스트 상품2").getId();
        Long categoryId1 = categoryRepository.findByName(categoryName1).getId();
        Long categoryId2 = categoryRepository.findByName(categoryName2).getId();
        productCategoryService.addProductCategory(productId1, categoryId1);
        productCategoryService.addProductCategory(productId2, categoryId2);
        Page<ProductResponseDto> productResponseDtoPage = productCategoryService.getProducts(List.of(categoryId1), 1);

        productResponseDtoPage.getContent().stream().forEach(productResponseDto -> {
            assertThat(productResponseDto.getName()).isEqualTo("테스트 상품1");
        });

        assertThat(productCategoryRepository.count()).isEqualTo(2);
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