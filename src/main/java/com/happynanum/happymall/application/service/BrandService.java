package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.entity.Brand;
import com.happynanum.happymall.domain.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public void addBrand(BrandRequestDto brandRequestDto) {
        String name = brandRequestDto.getName();
        duplicateBrandCheck(name);

        Brand brand = Brand.builder()
                .name(name)
                .description(brandRequestDto.getDescription())
                .productCount(0)
                .phoneNumber(brandRequestDto.getPhoneNumber())
                .build();

        brandRepository.save(brand);
        log.info("브랜드 추가 완료 = {}", brand.getName());
    }

    @Transactional
    public void modifyBrand(Long id, BrandRequestDto brandRequestDto) {
        String name = brandRequestDto.getName();

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드 식별자입니다 = " + id));

        if(!brandRequestDto.getName().equals(brand.getName()))
            duplicateBrandCheck(name);

        Brand modifiedBrand = Brand.builder()
                .id(brand.getId())
                .name(name)
                .description(brandRequestDto.getDescription())
                .productCount(brand.getProductCount())
                .phoneNumber(brandRequestDto.getPhoneNumber())
                .createdDate(brand.getCreatedDate())
                .build();

        brandRepository.save(modifiedBrand);
        log.info("브랜드 수정 완료 = {}", name);
    }

    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 브랜드 식별자입니다 = " + id));
        brandRepository.delete(brand);
        log.info("브랜드 삭제 완료 = {}", id);
    }

    private void duplicateBrandCheck(String name) {
        if (brandRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 브랜드 이름입니다 = " + name);
        }
    }
}

