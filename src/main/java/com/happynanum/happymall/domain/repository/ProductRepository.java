package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface
ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);
}
