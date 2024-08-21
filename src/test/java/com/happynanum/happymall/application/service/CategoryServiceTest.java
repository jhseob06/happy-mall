package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void afterEach() {
        categoryRepository.deleteAll();
    }

    @DisplayName("카테고리 추가 테스트")
    @Test
    void addCategory() {
        String name = "카테고리1";

        categoryService.addCategory(name);

        assertThat(categoryRepository.findAll().size()).isEqualTo(1);
    }

}