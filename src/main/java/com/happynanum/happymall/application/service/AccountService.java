package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.presentation.dto.JoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String joinProcess(JoinDto joinDto) {

        String identifier = joinDto.getIdentifier();

        if (accountRepository.existsByIdentifier(identifier)) {
            log.warn("이미 존재하는 아이디입니다 = {}", identifier);
            return "";
        }

        int age=0;

        if(joinDto.getBirth().isBefore(LocalDate.now()))
            age = LocalDate.now().getYear() - joinDto.getBirth().getYear()-1;
        else
            age = LocalDate.now().getYear() - joinDto.getBirth().getYear();


        Account account = Account.builder()
                .identifier(identifier)
                .name(joinDto.getName())
                .password(bCryptPasswordEncoder.encode(joinDto.getPassword()))
                .birth(joinDto.getBirth())
                .age(age)
                .phoneNumber(joinDto.getPhoneNumber())
                .height(joinDto.getHeight())
                .weight(joinDto.getWeight())
                .shoulderLength(joinDto.getShoulderLength())
                .armLength(joinDto.getArmLength())
                .wishLength(joinDto.getWishLength())
                .legLength(joinDto.getLegLength())
                .role("ROLE_MEMBER")
                .build();

        accountRepository.save(account);
        log.info("사용자 회원가입 완료 = {}",identifier);
        return identifier;
    }

}
