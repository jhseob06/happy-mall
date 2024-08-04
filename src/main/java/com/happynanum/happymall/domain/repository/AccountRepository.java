package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByIdentifier(String identifier);
}
