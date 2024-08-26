package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.AccountLike;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.AccountLikeRepository;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLikeService {

    private final AccountLikeRepository accountLikeRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addAccountLike(Long accountId, Long productId) {
         Account account = accountRepository.findById(accountId).orElseThrow(() ->
                 new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));
         Product product = productRepository.findById(productId).orElseThrow(() ->
                 new IllegalArgumentException("존재하지 않는 상품 식별자입니다 = " + productId));

        AccountLike accountLike = AccountLike.builder()
                .account(account)
                .product(product)
                .build();

        accountLikeRepository.save(accountLike);
        log.info("사용자 찜 추가 완료 = {}(사용자 식별자) {}(상품 식별자)",accountId, productId);
    }

    @Transactional
    public List<Product> getProducts(Long accountId) {
        List<Product> products = accountLikeRepository.findProductsByAccountId(accountId);
        log.info("사용자 찜 목록 조회 완료 = {}", accountId);
        return products;
    }

    @Transactional
    public void deleteAccountLike(Long accountId, Long productId) {
        AccountLike accountLike = accountLikeRepository.findByAccountIdAndProductId(accountId, productId).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 사용자 찜입니다 = " + accountId + " " + productId));
        accountLikeRepository.delete(accountLike);
        log.info("사용자 찜 삭제 완료 = {}", accountLike.getId());
    }

}
