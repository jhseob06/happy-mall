package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Page<Orders> findOrdersByAccount(Account account, Pageable pageable);

}
