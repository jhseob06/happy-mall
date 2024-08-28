package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;

import com.happynanum.happymall.domain.repository.BrandRepository;
import com.happynanum.happymall.domain.repository.CategoryRepository;
import com.happynanum.happymall.domain.repository.ProductCategoryRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
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

    @Autowired private BrandRepository brandRepository;

    @Autowired private ProductCategoryRepository productCategoryRepository;

    private Long categoryId;

    @BeforeEach
    @Transactional
    void beforeEach() {
        productCategoryRepository.deleteAll();
        productRepository.deleteAll();
        brandRepository.deleteAll();
        categoryRepository.deleteAll();

        categoryService.addCategory("product");
        categoryId = categoryRepository.findByName("product").getId();
    }

    @DisplayName("기본 상품 카테고리 조회 테스트")
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

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        Long productId1 = productRepository.findByName(productRequestDto1.getName()).getId();
        Long productId2 = productRepository.findByName(productRequestDto2.getName()).getId();

        System.out.println("categoryId = " + categoryId);
        System.out.println("productId1 = " + productId1);

        productCategoryService.addProductCategory(productId1, categoryId);
        productCategoryService.addProductCategory(productId2, categoryId);
        Page<ProductResponseDto> productResponseDtoPage = productCategoryService.getProducts(null, 1, null, null, null, null);

        assertThat(productResponseDtoPage.getContent().size()).isEqualTo(2);
    }

    @DisplayName("카테고리에 따른 상품 조회 테스트")
    @Test
    void getProductsByCategory() {
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
        ProductRequestDto productRequestDto3 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품3")
                .description("테스트 상품입니다.")

                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        productService.addProduct(productRequestDto3);
        categoryService.addCategory("summer");
        categoryService.addCategory("red");
        Long productId1 = productRepository.findByName(productRequestDto1.getName()).getId();
        Long productId2 = productRepository.findByName(productRequestDto2.getName()).getId();
        Long productId3 = productRepository.findByName(productRequestDto3.getName()).getId();
        Long categoryId1 = categoryRepository.findByName("summer").getId();
        Long categoryId2 = categoryRepository.findByName("red").getId();
        productCategoryService.addProductCategory(productId1, categoryId);
        productCategoryService.addProductCategory(productId2, categoryId);
        productCategoryService.addProductCategory(productId3, categoryId);
        productCategoryService.addProductCategory(productId1, categoryId1);
        productCategoryService.addProductCategory(productId2, categoryId1);
        productCategoryService.addProductCategory(productId3, categoryId2);
        Page<ProductResponseDto> productResponseDtoPage = productCategoryService.getProducts(List.of(categoryId1), 1, null, null, null, null);

        assertThat(productResponseDtoPage.getContent().size()).isEqualTo(2);

    }

    @DisplayName("가격 제한 테스트")
    @Test
    void priceLimitTest() {
        ProductRequestDto productRequestDto1 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품1")
                .description("테스트 상품입니다.")

                .price(50000)
                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto2 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품2")
                .description("테스트 상품입니다.")

                .price(10)
                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto3 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품3")
                .description("테스트 상품입니다.")

                .price(10000)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        productService.addProduct(productRequestDto3);
        Long productId1 = productRepository.findByName(productRequestDto1.getName()).getId();
        Long productId2 = productRepository.findByName(productRequestDto2.getName()).getId();
        Long productId3 = productRepository.findByName(productRequestDto3.getName()).getId();
        productCategoryService.addProductCategory(productId1, categoryId);
        productCategoryService.addProductCategory(productId2, categoryId);
        productCategoryService.addProductCategory(productId3, categoryId);
        Page<ProductResponseDto> productResponseDtoPage = productCategoryService.getProducts(null, 1, null, 500, 40000, null);

        productResponseDtoPage.getContent().stream().forEach(productResponseDto -> {
            assertThat(productResponseDto.getPrice()).isBetween(500, 40000);
        });
        assertThat(productResponseDtoPage.getContent().size()).isEqualTo(1);
    }

    @DisplayName("가격순 정렬 테스트")
    @Test
    void sortByPriceTest() {
        ProductRequestDto productRequestDto1 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품1")
                .description("테스트 상품입니다.")

                .price(50000)
                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto2 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품2")
                .description("테스트 상품입니다.")
                .price(10)

                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto3 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품3")
                .description("테스트 상품입니다.")
                .price(1000)

                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        productService.addProduct(productRequestDto3);
        Long productId1 = productRepository.findByName(productRequestDto1.getName()).getId();
        Long productId2 = productRepository.findByName(productRequestDto2.getName()).getId();
        Long productId3 = productRepository.findByName(productRequestDto3.getName()).getId();
        productCategoryService.addProductCategory(productId1, categoryId);
        productCategoryService.addProductCategory(productId2, categoryId);
        productCategoryService.addProductCategory(productId3, categoryId);
        Page<ProductResponseDto> productResponseDtoPage = productCategoryService.getProducts(null, 1, "lowPrice", null, null, null);

        assertThat(productResponseDtoPage.getContent().get(0).getPrice()).isEqualTo(10);
    }

    @DisplayName("검색 테스트")
    @Test
    void searchTest() {
        ProductRequestDto productRequestDto1 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품1")
                .description("테스트 상품입니다.")
                .price(50000)

                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto2 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품2")
                .description("테스트 상품입니다.")
                .price(10)

                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        Long productId1 = productRepository.findByName(productRequestDto1.getName()).getId();
        Long productId2 = productRepository.findByName(productRequestDto2.getName()).getId();
        productCategoryService.addProductCategory(productId1, categoryId);
        productCategoryService.addProductCategory(productId2, categoryId);
        Page<ProductResponseDto> productResponseDtoPage = productCategoryService.getProducts(null, 1, null, null, null, "테스트 상품1");

        assertThat(productResponseDtoPage.getContent().size()).isEqualTo(1);
    }

    @DisplayName("상품 삭제시 상품 카테고리 삭제 테스트")
    @Test
    void deleteProductTest() {
        ProductRequestDto productRequestDto1 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품1")
                .description("테스트 상품입니다.")

                .price(50000)
                .discount(0)
                .quantity(1)
                .build();
        ProductRequestDto productRequestDto2 = ProductRequestDto.builder()
                .brandName("테스트 브랜드")
                .name("테스트 상품2")
                .description("테스트 상품입니다.")

                .price(10)
                .discount(0)
                .quantity(1)
                .build();
        addBrand();

        productService.addProduct(productRequestDto1);
        productService.addProduct(productRequestDto2);
        Long productId1 = productRepository.findByName(productRequestDto1.getName()).getId();
        Long productId2 = productRepository.findByName(productRequestDto2.getName()).getId();
        productCategoryService.addProductCategory(productId1, categoryId);
        productCategoryService.addProductCategory(productId2, categoryId);
        productService.deleteProduct(productId1);

        assertThat(productCategoryRepository.count()).isEqualTo(1);
    }

    void addBrand() {
        BrandRequestDto brand1 = BrandRequestDto.builder()
                .name("테스트 브랜드")
                .description("테스트 브랜드입니다.")
                .phoneNumber("01012341234")
                .build();
        brandService.addBrand(brand1);
    }

}