package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.AccountService;
import com.happynanum.happymall.domain.dto.JoinDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinDto joinDto) {
        String identifier = accountService.joinProcess(joinDto);

        if (!identifier.equals(joinDto.getIdentifier())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
