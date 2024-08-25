package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.AccountLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountLikeRepository extends JpaRepository<AccountLike, Long> {
}
