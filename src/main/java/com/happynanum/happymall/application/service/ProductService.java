package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.product.ProductRequestDto;
import com.happynanum.happymall.domain.dto.product.ProductResponseDto;
import com.happynanum.happymall.domain.entity.Brand;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.BrandRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public ProductResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 상품 식별자입니다 = " + id));

        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .reviewCount(product.getReviewCount())
                .purchaseCount(product.getPurchaseCount())
                .build();

        log.info("상품 조회 성공 = {}(식별자) {}(이름)",id, product.getName());
        return productResponseDto;
    }

    @Transactional
    public void addProduct(ProductRequestDto productRequestDto) {
        String brandName = productRequestDto.getBrandName();
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 브랜드입니다 = " + brandName));

        Product product = Product.builder()
                .brand(brand)
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .quantity(productRequestDto.getQuantity())
                .discount(productRequestDto.getDiscount())
                .reviewCount(0)
                .purchaseCount(0)
                .build();

        productRepository.save(product);
        log.info("상품 추가 완료 = {}(식별자) {}(이름)",product.getId(), product.getName());
    }


}
