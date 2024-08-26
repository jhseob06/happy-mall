package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.address.AddressRequestDto;
import com.happynanum.happymall.domain.dto.address.AddressResponseDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Address;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void addAddress(Long accountId, AddressRequestDto addressRequestDto) {
        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));

        duplicateAddressCheck(addressRequestDto.getName());

        Address address = Address.builder()
                .account(account)
                .name(addressRequestDto.getName())
                .basicAddress(addressRequestDto.getBasicAddress())
                .detailedAddress(addressRequestDto.getDetailedAddress())
                .zoneCode(addressRequestDto.getZoneCode())
                .build();

        addressRepository.save(address);
        log.info("주소 추가 완료 = {}(사용자 식별자), {}(주소 이름)", accountId, address.getName());
    }

    @Transactional
    public void modifyAddress(Long addressId, AddressRequestDto addressRequestDto) {
        Long accountId = addressRequestDto.getAccountId();
        Account account = accountRepository.findById(accountId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 사용자 식별자입니다 = " + accountId));

        Address address = addressRepository.findById(addressId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 주소 식별자입니다 = " + addressId));

        if(!addressRequestDto.getName().equals(address.getName())) {
            duplicateAddressCheck(addressRequestDto.getName());
        }

        Address modifiedAddress = Address.builder()
                .id(address.getId())
                .name(addressRequestDto.getName())
                .basicAddress(addressRequestDto.getBasicAddress())
                .detailedAddress(addressRequestDto.getDetailedAddress())
                .zoneCode(addressRequestDto.getZoneCode())
                .createdDate(address.getCreatedDate())
                .build();

        addressRepository.save(modifiedAddress);
        log.info("주소 수정 완료 = {}(사용자 식별자), {}(주소 이름)", accountId, address.getName());
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 주소 식별자입니다 = " + addressId));

        addressRepository.delete(address);
        log.info("주소 삭제 완료 = {}(주소 식별자)", addressId);
    }

    @Transactional
    public Page<AddressResponseDto> getAddresses(Long accountId, int page) {
        Page<Address> addressPage =
                addressRepository.findAllByAccountId(accountId, PageRequest.of(page-1, 5));

        Page<AddressResponseDto> addressResponseDtoPage = addressPage.map(address ->
                AddressResponseDto.builder()
                        .id(address.getId())
                        .name(address.getName())
                        .basicAddress(address.getBasicAddress())
                        .detailedAddress(address.getDetailedAddress())
                        .zoneCode(address.getZoneCode())
                        .build());

        log.info("주소 목록 조회 성공 = {}(사용자 식별자)", accountId);
        return addressResponseDtoPage;
    }

    @Transactional
    public AddressResponseDto getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 주소 식별자입니다 = " + addressId));

        AddressResponseDto addressResponseDto = AddressResponseDto.builder()
                .id(address.getId())
                .name(address.getName())
                .basicAddress(address.getBasicAddress())
                .detailedAddress(address.getDetailedAddress())
                .zoneCode(address.getZoneCode())
                .build();

        log.info("주소 조회 성공 = {}(주소 식별자)", addressId);
        return addressResponseDto;
    }

    private void duplicateAddressCheck(String name) {
        if (addressRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 주소 이름입니다 = " + name);
        }
    }
}
