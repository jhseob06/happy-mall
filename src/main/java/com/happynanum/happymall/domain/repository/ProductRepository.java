package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface
ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("SELECT pc.product FROM ProductCategory pc WHERE pc.category.id IN :categoryIds")
    Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable);
}
