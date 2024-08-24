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

    @Query("SELECT pc.product FROM ProductCategory pc WHERE pc.category.id IN :categoryIds AND LOWER(CONCAT(pc.product.name, pc.product.description, pc.product.brand.name)) LIKE LOWER(CONCAT('%',:search,'%'))")
    Page<Product> findProductsByCategoryIds(List<Long> categoryIds, Pageable pageable, String search);

    @Query("SELECT pc.product FROM ProductCategory pc WHERE pc.category.id IN :categoryIds" +
            " AND pc.product.price BETWEEN :lowestPrice AND :highestPrice " +
            "AND LOWER(CONCAT(pc.product.name, pc.product.description, pc.product.brand.name)) " +
            "LIKE LOWER(CONCAT('%',:search,'%'))")
    Page<Product> findProductsByCategoryIdsAndPriceRange(List<Long> categoryIds, Integer lowestPrice, Integer highestPrice, Pageable pageable, String search);

}
