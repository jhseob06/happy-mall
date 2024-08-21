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
        Category category = Category.builder()
                .name(name)
                .build();
        categoryRepository.save(category);
        log.info("카테고리 추가 완료 = {}",name);
    }

}
