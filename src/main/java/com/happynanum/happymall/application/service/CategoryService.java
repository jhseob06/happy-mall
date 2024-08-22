package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.entity.Category;
import com.happynanum.happymall.domain.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addCategory(String name){
        duplicateCategoryCheck(name);
        Category category = Category.builder()
                .name(name)
                .build();
        categoryRepository.save(category);
        log.info("카테고리 추가 완료 = {}",name);
    }

    @Transactional
    public void modifyCategory(Long id, String name){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 식별자입니다 = " + id));

        if(!name.equals(category.getName()))
            duplicateCategoryCheck(name);

        Category modifiedCategory = Category.builder()
                .id(category.getId())
                .name(name)
                .build();
        categoryRepository.save(modifiedCategory);
        log.info("카테고리 수정 완료 = {}", name);
    }

    @Transactional
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 식별자입니다 = " + id));
        categoryRepository.delete(category);
        log.info("카테고리 삭제 완료 = {}", id);
    }

    private void duplicateCategoryCheck(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다 = " + name);
        }
    }
}
