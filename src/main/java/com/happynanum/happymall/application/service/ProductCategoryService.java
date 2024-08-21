package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Category;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.entity.ProductCategory;
import com.happynanum.happymall.domain.repository.CategoryRepository;
import com.happynanum.happymall.domain.repository.ProductCategoryRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCategoryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Transactional
    public void addProductCategory(Long id, Long categoryId) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 상품 식별자입니다 = " + id));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 카테고리 식별자입니다 = " + categoryId));

        ProductCategory productCategory = ProductCategory.builder()
                .product(product)
                .category(category)
                .build();

        productCategoryRepository.save(productCategory);
        log.info("상품 카테고리 추가 완료 = {}(상품 식별자) {}(카테고리 식별자)",id, categoryId);
    }

    @Transactional
    public Page<ProductResponseDto> getProducts(List<Long> categoryIds, int page) {
        Page<Product> productPage = productRepository.findProductsByCategoryIds(categoryIds, PageRequest.of(5*(page-1), 5*page));
        Page<ProductResponseDto> productResponseDtoPage = productPage.map(product ->
                ProductResponseDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .reviewCount(product.getReviewCount())
                        .purchaseCount(product.getPurchaseCount())
                        .discount(product.getDiscount())
                        .build()
        );
        log.info("상품 목록 조회 성공 = {}(페이지)",page);
        return productResponseDtoPage;
    }
}
