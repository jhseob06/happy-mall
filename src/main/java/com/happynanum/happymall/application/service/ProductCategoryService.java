package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandResponseDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<ProductResponseDto> getProducts(List<Long> categoryIds, int page, String sort, Integer lowestPrice, Integer highestPrice, String search) {

        Pageable pageable = PageRequest.of(page-1, 5);
        Page<Product> productPage;

        if (search == null) search = "";

        if (categoryIds==null) {
            categoryIds = List.of(categoryRepository.findByName("product").getId());
        }

        for (Long categoryId : categoryIds) {
            if(categoryId>=53 && categoryId<=58) {
                categoryIds.removeIf(id -> id>=59 && id<=68);
                break;
            }
            else if(categoryId>=59 && categoryId<=68) {
                categoryIds.removeIf(id -> id>=53 && id<=58);
                break;
            }
        }

        int categoryCount = categoryIds.size();

        if ("lowPrice".equalsIgnoreCase(sort)) {
            Sort sortByHighestPrice = Sort.by(Sort.Order.asc("product.price"));
            pageable = PageRequest.of(5*(page-1), 5*page, sortByHighestPrice);
        }
        else if("highPrice".equalsIgnoreCase(sort)) {
            Sort sortByLowestPrice = Sort.by(Sort.Order.desc("product.price"));
            pageable = PageRequest.of(5*(page-1), 5*page, sortByLowestPrice);
        }
        else if("reviewCount".equalsIgnoreCase(sort)) {
            Sort sortByReviewCount = Sort.by(Sort.Order.desc("product.reviewCount"));
            pageable = PageRequest.of(5*(page-1), 5*page, sortByReviewCount);
        }
        else if("purchaseCount".equalsIgnoreCase(sort)) {
            Sort sortByPurchaseCount = Sort.by(Sort.Order.desc("product.purchaseCount"));
            pageable = PageRequest.of(5*(page-1), 5*page, sortByPurchaseCount);
        }

        if (lowestPrice != null && highestPrice != null) {
            productPage = productRepository.findProductsByCategoryIdsAndPriceRange(categoryIds, categoryCount, lowestPrice, highestPrice, pageable, search);
        } else {
            productPage = productRepository.findProductsByCategoryIds(categoryIds, categoryCount, pageable, search);
        }

        Page<ProductResponseDto> productResponseDtoPage =
                productPage.map(this::productToProductResponseDto);

        log.info("상품 목록 조회 성공 = {}(페이지)", page);
        return productResponseDtoPage;
    }

    @Transactional
    public void deleteProductCategory(Long id) {
        ProductCategory productCategory = productCategoryRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 상품 카테고리 식별자입니다 = " + id));
        productCategoryRepository.delete(productCategory);
        log.info("상품 카테고리 삭제 완료 = {}", id);
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
