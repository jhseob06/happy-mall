package com.happynanum.happymall.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final AccountRepository accountRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    private final Map<String, Object> orderData = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${project.domain}")
    private String domain;
    @Value("${toss_pay.sk}")
    private String apiKey;
    @Value("${toss_pay.payment_url}")
    private String paymentUrl;
    @Value("${toss_pay.execute_url}")
    private String executeUrl;

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

        orderData.put(orderNo + "payToken", payToken);
        orderData.put(orderNo + "product", product);
        orderData.put(orderNo + "account", account);
        orderData.put(orderNo + "address", address);
        orderData.put(orderNo + "quantity", ordersRequestDto.getQuantity());

        log.info("결제 생성에 성공했습니다 = {}(orderNo) {}(checkoutPage)", orderNo, checkoutPage);
        return orderNo + "," + checkoutPage;

    }

    @Transactional
    public void successLogic(String orderNo) throws JsonProcessingException {
        if (!orderData.containsKey(orderNo + "payToken")) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        }

        String payToken = (String) orderData.get(orderNo + "payToken");
        OrdersTransmitDto orderTransmitDto = OrdersTransmitDto.builder()
                .apiKey(apiKey)
                .orderNo(orderNo)
                .payToken(payToken)
                .build();

        JsonNode jsonNode = runUrl(executeUrl, orderTransmitDto);

        if(jsonNode.get("code").asInt() != 0) {
            throw new IllegalArgumentException("결제 진행에 실패하였습니다. = " + jsonNode.get("msg").asText());
        }

        Product product = (Product) orderData.get(orderNo + "product");
        Account account = (Account) orderData.get(orderNo + "account");
        Address address = (Address) orderData.get(orderNo + "address");
        int quantity = (int) orderData.get(orderNo + "quantity");

        product.purchaseProduct(quantity);
        productRepository.save(product);
        log.info("상품 구매수 및 수량 조정 완료 = {}(상품 아이디)", product.getId());

        Orders order = Orders.builder()
                .product(product)
                .account(account)
                .address(address)
                .quantity(quantity)
                .deliveryStatus("결제완료")
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
        ordersRepository.save(order);

        orderData.remove(orderNo + "product");
        orderData.remove(orderNo + "account");
        orderData.remove(orderNo + "address");
        orderData.remove(orderNo + "quantity");
        orderData.remove(orderNo + "payToken");

        log.info("결제 완료 = {}", order.getId());
    }

    public void cancelLogic(String orderNo) throws JsonProcessingException {
        if (!orderData.containsKey(orderNo + "payToken")) {
            throw new IllegalArgumentException("결제 정보가 존재하지 않습니다.");
        };

        orderData.remove(orderNo + "product");
        orderData.remove(orderNo + "account");
        orderData.remove(orderNo + "address");
        orderData.remove(orderNo + "quantity");
        orderData.remove(orderNo + "payToken");

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
                .address(
                        AddressResponseDto.builder()
                                .id(order.getAddress().getId())
                                .name(order.getAddress().getName())
                                .basicAddress(order.getAddress().getBasicAddress())
                                .detailedAddress(order.getAddress().getDetailedAddress())
                                .zoneCode(order.getAddress().getZoneCode())
                                .build()
                )
                .orderNo(order.getOrderNo())
                .productDesc(order.getProduct().getName())
                .quantity(order.getQuantity())
                .deliveryStatus(order.getDeliveryStatus())
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
