package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.AccountLike;
import com.happynanum.happymall.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountLikeRepository extends JpaRepository<AccountLike, Long> {

    @Query("SELECT al.product FROM AccountLike al WHERE al.account.id = :accountId")
    List<Product> findProductsByAccountId(Long accountId);

    Optional<AccountLike> findByAccountIdAndProductId(Long accountId, Long productId);
}
