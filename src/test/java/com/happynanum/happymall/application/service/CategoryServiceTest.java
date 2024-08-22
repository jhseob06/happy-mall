package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void beforeEach() {
        categoryRepository.deleteAll();
    }

    @DisplayName("카테고리 추가 테스트")
    @Test
    void addCategory() {
        String name = "카테고리1";

        categoryService.addCategory(name);

        assertThat(categoryRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("카테고리 중복 테스트")
    @Test
    void duplicateCategoryTest() {
        String name = "카테고리1";

        categoryService.addCategory(name);
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.addCategory(name));
    }

    @DisplayName("카테고리 수정 테스트")
    @Test
    void modifyCategory() {
        String name = "카테고리1";
        String modifiedName = "카테고리2";

        categoryService.addCategory(name);
        Long id = categoryRepository.findByName(name).getId();
        categoryService.modifyCategory(id, modifiedName);

        assertThat(categoryRepository.findById(id).get().getName()).isEqualTo(modifiedName);
    }

    @DisplayName("카테고리 삭제 테스트")
    @Test
    void deleteCategory() {
        String name = "카테고리1";

        categoryService.addCategory(name);
        Long id = categoryRepository.findByName(name).getId();
        categoryService.deleteCategory(id);

        assertThat(categoryRepository.findAll().size()).isEqualTo(0);
    }

}