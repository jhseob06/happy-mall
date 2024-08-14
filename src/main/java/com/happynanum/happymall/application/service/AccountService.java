package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.AccountRequestDto;
import com.happynanum.happymall.domain.dto.AccountResponseDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.dto.JoinDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinProcess(JoinDto joinDto) {

        String identifier = joinDto.getIdentifier();
        duplicateAccountCheck(identifier);

        int age;

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
                .waistLength(joinDto.getWaistLength())
                .legLength(joinDto.getLegLength())
                .role("ROLE_MEMBER")
                .build();

        accountRepository.save(account);
        log.info("사용자 회원가입 완료 = {}",identifier);
    }

    @Transactional
    public void modifyAccount(Long id, AccountRequestDto accountRequestDto) {
        String identifier = accountRequestDto.getIdentifier();
        Account account = accountRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 회원 식별자입니다 = " + id));
        String accountIdentifier = account.getIdentifier();

        if (!accountIdentifier.equals(identifier)) {
            duplicateAccountCheck(identifier);
        }

        Account updateAccount = Account.builder()
                .id(id)
                .identifier(identifier)
                .name(accountRequestDto.getName())
                .password(bCryptPasswordEncoder.encode(accountRequestDto.getPassword()))
                .birth(accountRequestDto.getBirth())
                .age(accountRequestDto.getAge())
                .phoneNumber(accountRequestDto.getPhoneNumber())
                .height(accountRequestDto.getHeight())
                .weight(accountRequestDto.getWeight())
                .shoulderLength(accountRequestDto.getShoulderLength())
                .armLength(accountRequestDto.getArmLength())
                .waistLength(accountRequestDto.getWaistLength())
                .legLength(accountRequestDto.getLegLength())
                .createdDate(account.getCreatedDate())
                .modifiedDate(LocalDateTime.now())
                .role(account.getRole())
                .build();

        accountRepository.save(updateAccount);

        log.info("사용자 정보수정 완료 = (기존아이디){} (변경후아이디){}", accountIdentifier, identifier);
    }

    public AccountResponseDto getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->
                new IllegalArgumentException("존재하지 않는 회원 식별자입니다 = " + id));

        return AccountResponseDto.builder()
                .identifier(account.getIdentifier())
                .name(account.getName())
                .birth(account.getBirth())
                .age(account.getAge())
                .phoneNumber(account.getPhoneNumber())
                .height(account.getHeight())
                .weight(account.getWeight())
                .shoulderLength(account.getShoulderLength())
                .armLength(account.getArmLength())
                .waistLength(account.getWaistLength())
                .legLength(account.getLegLength())
                .build();
    }

    public void duplicateAccountCheck(String identifier){
        if (accountRepository.existsByIdentifier(identifier)) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다 = " + identifier);
        }
    }

}
