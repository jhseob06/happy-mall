package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.brand.BrandRequestDto;
import com.happynanum.happymall.domain.entity.Brand;
import com.happynanum.happymall.domain.repository.BrandRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BrandServiceTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandRepository brandRepository;

    @AfterEach
    void afterEach() {
        brandRepository.deleteAll();
    }

    @DisplayName("브랜드 등록 테스트")
    @Test
    void brandAddTest() {
        BrandRequestDto brandRequestDto = BrandRequestDto.builder()
                .name("뽀송")
                .description("뽀송뽀송한 브랜드")
                .phoneNumber("01012341234")
                .build();

        brandService.addBrand(brandRequestDto);
        Brand brand = brandRepository.findByName(brandRequestDto.getName()).get();

        assertThat(brand.getDescription()).isEqualTo(brandRequestDto.getDescription());
    }

    @DisplayName("브랜드 중복 테스트")
    @Test
    void brandDuplicateTest() {
        BrandRequestDto brandRequestDto1 = BrandRequestDto.builder()
                .name("뽀송")
                .description("뽀송뽀송한 브랜드")
                .phoneNumber("01012341234")
                .build();
        BrandRequestDto brandRequestDto2 = BrandRequestDto.builder()
                .name("뽀송")
                .description("뽀송뽀송한 브랜드")
                .phoneNumber("01012341234")
                .build();

        brandService.addBrand(brandRequestDto1);
        assertThrows(IllegalArgumentException.class,
                () -> brandService.addBrand(brandRequestDto2));
    }

    @DisplayName("브랜드 수정 테스트")
    @Test
    void brandModifyTest() {
        BrandRequestDto brandRequestDto = BrandRequestDto.builder()
                .name("뽀송")
                .description("뽀송뽀송한 브랜드")
                .phoneNumber("01012341234")
                .build();
        BrandRequestDto modifyBrandRequestDto = BrandRequestDto.builder()
                .name("뽀송2")
                .description("뽀송뽀송한 브랜드 수정")
                .phoneNumber("01012341234")
                .build();

        brandService.addBrand(brandRequestDto);
        Brand brand = brandRepository.findByName(brandRequestDto.getName()).get();
        brandService.modifyBrand(brand.getId(), modifyBrandRequestDto);
        Brand modifiedBrand = brandRepository.findByName(modifyBrandRequestDto.getName()).get();

        assertThat(modifiedBrand.getDescription()).isEqualTo(modifyBrandRequestDto.getDescription());
    }

    @DisplayName("브랜드 삭제 테스트")
    @Test
    void brandDeleteTest() {
        BrandRequestDto brandRequestDto = BrandRequestDto.builder()
                .name("뽀송")
                .description("뽀송뽀송한 브랜드")
                .phoneNumber("01012341234")
                .build();

        brandService.addBrand(brandRequestDto);
        Brand brand = brandRepository.findByName(brandRequestDto.getName()).get();
        brandService.deleteBrand(brand.getId());

        assertThat(brandRepository.findByName(brandRequestDto.getName())).isEmpty();
    }
}