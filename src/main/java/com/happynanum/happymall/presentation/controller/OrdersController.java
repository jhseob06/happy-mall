package com.happynanum.happymall.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.happynanum.happymall.application.service.OrdersService;
import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.dto.order.OrdersRequestDto;
import com.happynanum.happymall.domain.dto.order.OrdersResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @PostMapping("/orders")
    public ResponseEntity<?> createPayment(@RequestBody @Valid OrdersRequestDto ordersRequestDto) throws JsonProcessingException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();

        String checkoutPage = ordersService.createPayment(ordersRequestDto, accountId);

        return ResponseEntity.ok().body(checkoutPage);
    }

    @GetMapping("/orders/success")
    public ResponseEntity<?> successPayment(@RequestParam String orderNo) throws JsonProcessingException {
        ordersService.successLogic(orderNo);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/orders/cancel")
    public ResponseEntity<?> cancelPayment(@RequestParam String orderNo) throws JsonProcessingException {
        ordersService.cancelLogic(orderNo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestParam int page) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Long accountId = customUserDetails.getId();
        Page<OrdersResponseDto> orders = ordersService.getOrders(accountId, page);
        return ResponseEntity.ok().body(orders);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<?> patchOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) Long addressId,
            @RequestParam(required = false) String deliveryStatus) {
        if(addressId != null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();
            Long accountId = customUserDetails.getId();
            ordersService.modifyAddress(addressId, orderId, accountId);
        }
        else if(deliveryStatus != null) {
            ordersService.modifyDeliveryStatus(deliveryStatus, orderId);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
