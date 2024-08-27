package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Cart;
import com.happynanum.happymall.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsByAccountAndProduct(Account account, Product product);

    List<Cart> findCartsByAccount(Account account);

}
