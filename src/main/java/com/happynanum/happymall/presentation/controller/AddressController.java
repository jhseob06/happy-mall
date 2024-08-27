package com.happynanum.happymall.presentation.controller;

import com.happynanum.happymall.application.service.AddressService;
import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.dto.address.AddressRequestDto;
import com.happynanum.happymall.domain.dto.address.AddressResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody @Valid AddressRequestDto addressRequestDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        addressService.addAddress(accountId, addressRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> modifyAddress(
            @RequestBody @Valid AddressRequestDto addressRequestDto,
            @PathVariable Long addressId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        addressService.modifyAddress(accountId, addressId, addressRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        addressService.deleteAddress(accountId, addressId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAddresses(@RequestParam int page) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        Page<AddressResponseDto> addressPage = addressService.getAddresses(accountId, page);

        return ResponseEntity.ok().body(addressPage);
    }


}
