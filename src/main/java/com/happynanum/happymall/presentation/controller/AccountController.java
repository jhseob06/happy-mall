package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.AccountService;
import com.happynanum.happymall.domain.dto.account.AccountRequestDto;
import com.happynanum.happymall.domain.dto.account.AccountResponseDto;
import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.dto.JoinDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinDto joinDto) {
        accountService.joinProcess(joinDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/member")
    public ResponseEntity<?> modify(
            @RequestBody @Valid AccountRequestDto accountRequestDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long id = customUserDetails.getId();

        accountService.modifyAccount(id, accountRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/member")
    public ResponseEntity<?> get() {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long id = customUserDetails.getId();

        AccountResponseDto account = accountService.getAccount(id);
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
}
