package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.AddressService;
import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.dto.address.AddressRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<?> addAddress(@RequestBody @Valid AddressRequestDto addressRequestDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        addressService.addAddress(accountId, addressRequestDto);
        return ResponseEntity.ok().build();
    }


}
