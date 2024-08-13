package com.happynanum.happymall.application.service;
import com.happynanum.happymall.domain.dto.AccountRequestDto;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

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
                .waistLength(60)
                .legLength(120)
                .build();

        accountService.joinProcess(joinDto);
        Account account = accountRepository.findByIdentifier(joinDto.getIdentifier());

        assertThat(account.getName()).isEqualTo(joinDto.getName());
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
                .waistLength(60)
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
                .waistLength(60)
                .legLength(120)
                .build();

        accountService.joinProcess(joinDto1);

        assertThrows(IllegalArgumentException.class, () ->
                accountService.joinProcess(joinDto2));


    }

    @Test
    @DisplayName("사용자 정보수정 테스트")
    public void modifyAccount() {

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
                .waistLength(60)
                .legLength(120)
                .build();
        AccountRequestDto accountRequestDto = AccountRequestDto.builder()
                .identifier("modifyMember")
                .name("modifyMember")
                .password("qwer1234")
                .birth(LocalDate.of(2006, 12, 26))
                .phoneNumber("01012341234")
                .age(19)
                .height(180)
                .weight(70)
                .shoulderLength(80)
                .armLength(90)
                .waistLength(60)
                .legLength(120)
                .build();

        accountService.joinProcess(joinDto);
        Long id = accountRepository.findByIdentifier(joinDto.getIdentifier()).getId();
        accountService.modifyAccount(id, accountRequestDto);
        Account account = accountRepository.findById(id).get();

        assertThat(account.getIdentifier()).isEqualTo("modifyMember");
        assertThat(account.getName()).isEqualTo("modifyMember");
        assertThat(accountRepository.count()).isEqualTo(1);
    }
}