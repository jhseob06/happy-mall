package com.happynanum.happymall.application.service;

import com.happynanum.happymall.domain.dto.JoinDto;
import com.happynanum.happymall.domain.dto.address.AddressRequestDto;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AddressServiceTest {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void beforeEach() {
        addressRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @DisplayName("주소 추가 테스트")
    @Test
    void addAddressTest() {
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
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .name("테스트주소")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();

        accountService.joinProcess(joinDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        addressService.addAddress(accountId, addressRequestDto);

        assertEquals(addressRepository.findAll().size(), 1);
    }

    @DisplayName("주소 수정 테스트")
    @Test
    void modifyAddressTest() {
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
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .name("테스트주소")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();
        AddressRequestDto modifyAddressRequestDto = AddressRequestDto.builder()
                .name("수정주소")
                .basicAddress("서울시 강남구")
                .detailedAddress("수정상세주소")
                .zoneCode(12345)
                .build();

        accountService.joinProcess(joinDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        addressService.addAddress(accountId, addressRequestDto);
        Long addressId = addressRepository.findByName("테스트주소").getId();
        addressService.modifyAddress(accountId, addressId, modifyAddressRequestDto);

        assertEquals(addressRepository.findById(addressId).get().getName(), "수정주소");
    }

    @DisplayName("주소 삭제 테스트")
    @Test
    void deleteAddressTest() {
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
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .name("테스트주소")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();

        accountService.joinProcess(joinDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        addressService.addAddress(accountId, addressRequestDto);
        Long addressId = addressRepository.findByName("테스트주소").getId();
        addressService.deleteAddress(accountId, addressId);

        assertThat(addressRepository.findAll().size()).isEqualTo(0);
    }

    @DisplayName("주소 이름 중복 테스트")
    @Test
    void duplicateAddressTest() {
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
        AddressRequestDto addressRequestDto = AddressRequestDto.builder()
                .name("테스트주소")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();
        AddressRequestDto duplicateAddressRequestDto = AddressRequestDto.builder()
                .name("테스트주소")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();

        accountService.joinProcess(joinDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        addressService.addAddress(accountId, addressRequestDto);

        assertThrows(IllegalArgumentException.class, () -> addressService.addAddress(accountId, duplicateAddressRequestDto));
    }

    @DisplayName("주소 목록 조회 테스트")
    @Test
    void getProductsTest() {
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
        AddressRequestDto addressRequestDto1 = AddressRequestDto.builder()
                .name("테스트주소1")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();
        AddressRequestDto addressRequestDto2 = AddressRequestDto.builder()
                .name("테스트주소2")
                .basicAddress("서울시 강남구")
                .detailedAddress("테스트상세주소")
                .zoneCode(12345)
                .build();

        accountService.joinProcess(joinDto);
        Long accountId = accountRepository.findByIdentifier("member").getId();
        addressService.addAddress(accountId, addressRequestDto1);
        addressService.addAddress(accountId, addressRequestDto2);

        assertThat(addressService.getAddresses(accountId, 1).get().count()).isEqualTo(2);
    }
}