package com.happynanum.happymall.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happynanum.happymall.domain.dto.address.AddressData;
import com.happynanum.happymall.domain.dto.address.AddressResponseDto;
import com.happynanum.happymall.domain.dto.order.OrdersResponseDto;
import com.happynanum.happymall.domain.dto.order.OrdersTransmitDto;
import com.happynanum.happymall.domain.dto.order.OrdersRequestDto;
import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Address;
import com.happynanum.happymall.domain.entity.Orders;
import com.happynanum.happymall.domain.entity.Product;
import com.happynanum.happymall.domain.repository.AccountRepository;
import com.happynanum.happymall.domain.repository.AddressRepository;
import com.happynanum.happymall.domain.repository.OrdersRepository;
import com.happynanum.happymall.domain.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final AccountRepository accountRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${project.domain}")
    private String domain;
    @Value("${toss_pay.sk}")
    private String apiKey;
    @Value("${toss_pay.payment_url}")
    private String paymentUrl;
    @Value("${toss_pay.execute_url}")
    private String executeUrl;
    @Value("${toss_pay.refund_url}")
    private String refundUrl;

    @Transactional
    public String createPayment(OrdersRequestDto ordersRequestDto, Long accountId) throws JsonProcessingException {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Product product = productRepository.findById(ordersRequestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        Address address = addressRepository.findById(ordersRequestDto.getAddressId())
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 존재하지 않습니다."));

        if (product.getQuantity() < ordersRequestDto.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        UUID orderNo = UUID.randomUUID();

        int price = product.getPrice();
        OrdersTransmitDto orderTransmitDto = OrdersTransmitDto.builder()
                .apiKey(apiKey)
                .orderNo(orderNo.toString())
                .amount((price - (price*(product.getDiscount()))) * ordersRequestDto.getQuantity())
                .amountTaxFree(0)
                .productDesc(product.getName())
                .retUrl(domain + "/orders/success")
                .retCancelUrl(domain + "/orders/cancel")
                .build();

        JsonNode responseNode = runUrl(paymentUrl, orderTransmitDto);

        if(responseNode.get("code").asInt() != 0) {
            throw new IllegalArgumentException("결제 생성에 실패하였습니다. = " + responseNode.get("msg").asText());
        }

        String payToken = responseNode.get("payToken").asText();
        String checkoutPage = responseNode.get("checkoutPage").asText();

        log.info(payToken);
        log.info(product.getId().toString());
        log.info(account.getId().toString());
        log.info(address.getId().toString());
        log.info(ordersRequestDto.getQuantity().toString());
        log.info(ordersRequestDto.getSize());

        redisTemplate.opsForValue().set("order:payToken:"+orderNo, payToken, 3600000, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("order:product:"+orderNo, product.getId().toString(), 3600000, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("order:account:"+orderNo, account.getId().toString(), 3600000, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("order:address:"+orderNo, address.getId().toString(), 3600000, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("order:quantity:"+orderNo, ordersRequestDto.getQuantity().toString(), 3600000, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("order:size:"+orderNo, ordersRequestDto.getSize(), 3600000, TimeUnit.MILLISECONDS);

        log.info("결제 생성에 성공했습니다 = {}(orderNo) {}(checkoutPage)", orderNo, checkoutPage);
        return orderNo + "," + checkoutPage;

    }

    @Transactional
    public void successLogic(String orderNo) throws JsonProcessingException {
        if (!redisTemplate.hasKey("order:payToken:"+orderNo)) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        String payToken = redisTemplate.opsForValue().get("order:payToken:"+orderNo);
        OrdersTransmitDto orderTransmitDto = OrdersTransmitDto.builder()
                .apiKey(apiKey)
                .orderNo(orderNo)
                .payToken(payToken)
                .build();

        JsonNode jsonNode = runUrl(executeUrl, orderTransmitDto);

        if(jsonNode.get("code").asInt() != 0) {
            throw new IllegalArgumentException("결제 진행에 실패하였습니다. = " + jsonNode.get("msg").asText());
        }

        Long productId = Long.parseLong(redisTemplate.opsForValue().get("order:product:"+orderNo));
        Long accountId = Long.parseLong(redisTemplate.opsForValue().get("order:account:"+orderNo));
        Long addressId = Long.parseLong(redisTemplate.opsForValue().get("order:address:"+orderNo));
        int quantity = Integer.parseInt(redisTemplate.opsForValue().get("order:quantity:"+orderNo));
        String size = redisTemplate.opsForValue().get("order:size:"+orderNo);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 존재하지 않습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        if(product.getQuantity() < 0) {
            runUrl(refundUrl, orderTransmitDto);
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        product.purchaseProduct(quantity);

        Orders order = Orders.builder()
                .productId(product.getId())
                .productName(product.getName())
                .addressData(
                        AddressData.builder()
                                .basicAddress(address.getBasicAddress())
                                .detailedAddress(address.getDetailedAddress())
                                .zoneCode(address.getZoneCode())
                                .build()

                )
                .account(account)
                .quantity(quantity)
                .deliveryStatus("결제완료")
                .size(size)
                .code(jsonNode.get("code").asInt())
                .mode(jsonNode.get("mode").asText())
                .stateMsg(jsonNode.get("stateMsg").asText())
                .amount(jsonNode.get("amount").asInt())
                .discountAmount(product.getDiscount()*quantity)
                .paidAmount(jsonNode.get("paidAmount").asInt())
                .payMethod(jsonNode.get("payMethod").asText())
                .orderNo(jsonNode.get("orderNo").asText())
                .payToken(jsonNode.get("payToken").asText())
                .transactionId(jsonNode.get("transactionId").asText())
                .createdDate(LocalDateTime.parse(
                        jsonNode.get("approvalTime").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ))
                .build();

        if (order.getPayMethod().equals("CARD")) {
            log.info("카드");
            order.cardOrder(jsonNode);
        }
        else if(order.getPayMethod().equals("TOSS_MONEY")) {
            log.info("토스머니");
            order.tossMoneyOrder(jsonNode);
        }

        redisTemplate.delete("order:payToken:"+orderNo);
        redisTemplate.delete("order:product:"+orderNo);
        redisTemplate.delete("order:account:"+orderNo);
        redisTemplate.delete("order:address:"+orderNo);
        redisTemplate.delete("order:quantity:"+orderNo);
        redisTemplate.delete("order:size:"+orderNo);

        try{
            ordersRepository.save(order);
        }
        catch (Exception e){
            runUrl(refundUrl, orderTransmitDto);
            throw new IllegalArgumentException("주문 정보 저장에 실패하였습니다.");
        }

        try {
            productRepository.save(product);
        }catch (Exception e) {
            runUrl(refundUrl, orderTransmitDto);
            throw new IllegalArgumentException("상품 구매수 및 수량 조정에 실패하였습니다.");
        }

        log.info("상품 구매수 및 수량 조정 완료 = {}(상품 아이디)", product.getId());

        log.info("결제 완료 = {}", order.getId());
    }

    public void cancelLogic(String orderNo) throws JsonProcessingException {
        if (!redisTemplate.hasKey("payToken:"+orderNo)) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        redisTemplate.delete("order:payToken:"+orderNo);
        redisTemplate.delete("order:product:"+orderNo);
        redisTemplate.delete("order:account:"+orderNo);
        redisTemplate.delete("order:address:"+orderNo);
        redisTemplate.delete("order:quantity:"+orderNo);
        redisTemplate.delete("order:size:"+orderNo);

        log.info("결제 취소 완료 = {}", orderNo);
    }

    @Transactional
    public Page<OrdersResponseDto> getOrders(Long accountId, int page) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Pageable pageable = PageRequest.of(page-1, 5);

        Page<Orders> orders = ordersRepository.findOrdersByAccount(account, pageable);

        Page<OrdersResponseDto> orderResponseDtos = orders
                .map(this::orderToOrderResponseDto);

        log.info("주문 목록 조회 완료 = {}", accountId);
        return orderResponseDtos;
    }

    @Transactional
    public void modifyAddress(Long addressId, Long orderId, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 존재하지 않습니다."));

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        if(!orders.getAccount().equals(account)) {
            throw new IllegalArgumentException("해당 주문은 사용자의 주문이 아닙니다 = " + orderId);
        }

        if (!address.getAccount().equals(account)) {
            throw new IllegalArgumentException("해당 주소는 사용자의 주소가 아닙니다 = " + addressId);
        }

        orders.updateAddress(address);

        ordersRepository.save(orders);
        log.info("주소 수정 완료 = {}(주문번호) {}(주소아이디)", orders.getOrderNo(), addressId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void modifyDeliveryStatus(String deliveryStatus, Long orderId) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        orders.updateDeliveryStatus(deliveryStatus);

        ordersRepository.save(orders);
        log.info("배송상태 수정 완료 = {}(주문번호) {}(배송상태)", orders.getOrderNo(), deliveryStatus);
    }

    private OrdersResponseDto orderToOrderResponseDto(Orders order) {
        return OrdersResponseDto.builder()
                .id(order.getId())
                .address(
                        AddressData.builder()
                                .basicAddress(order.getAddressData().getBasicAddress())
                                .detailedAddress(order.getAddressData().getDetailedAddress())
                                .zoneCode(order.getAddressData().getZoneCode())
                                .build()
                )
                .productId(order.getProductId())
                .orderNo(order.getOrderNo())
                .productDesc(order.getProductName())
                .quantity(order.getQuantity())
                .deliveryStatus(order.getDeliveryStatus())
                .size(order.getSize())
                .amount(order.getAmount())
                .discountAmount(order.getDiscountAmount())
                .payMethod(order.getPayMethod())
                .build();
    }

    private JsonNode runUrl(String url, OrdersTransmitDto orderTransmitDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<OrdersTransmitDto> requestEntity = new HttpEntity<>(orderTransmitDto, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return objectMapper.readTree(responseEntity.getBody());
    }

}
