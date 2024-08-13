package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.AccountService;
import com.happynanum.happymall.domain.dto.AccountRequestDto;
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<?> modify(
            @PathVariable Long id,
            @RequestBody @Valid AccountRequestDto accountRequestDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!customUserDetails.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        accountService.modifyAccount(id, accountRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/main")
    public String main() {
        return "hello";
    }
}
