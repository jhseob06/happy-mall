package com.happynanum.happymall.application.service;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.dto.JoinDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService joinService;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void joinTest() {
        JoinDto joinDto = JoinDto.builder()
                .identifier("member")
                .name("member")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .wishLength(60)
                .legLength(120)
                .build();

        joinService.joinProcess(joinDto);
        Account account = accountRepository.findByIdentifier(joinDto.getIdentifier());

        Assertions.assertThat(account.getName()).isEqualTo(joinDto.getName());
    }

    @Test
    @DisplayName("회원가입 아이디 중복 테스트")
    void duplicateIdentifier() {
        JoinDto joinDto1 = JoinDto.builder()
                .identifier("member")
                .name("member1")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .wishLength(60)
                .legLength(120)
                .build();

        JoinDto joinDto2 = JoinDto.builder()
                .identifier("member")
                .name("member2")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .wishLength(60)
                .legLength(120)
                .build();

        joinService.joinProcess(joinDto1);
        joinService.joinProcess(joinDto2);

        Assertions.assertThat(accountRepository.count()).isEqualTo(1);
    }
}