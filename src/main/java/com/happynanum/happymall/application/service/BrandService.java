package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.entity.Brand;
import com.happynanum.happymall.domain.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private void duplicateBrandCheck(String name) {
        if (brandRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 브랜드 이름입니다 = " + name);
        }
    }
}

