package com.happynanum.happymall.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @NotNull
    private Integer quantity;

    @NotBlank
    private String deliveryStatus;

    @NotNull
    private Integer code;

    @NotBlank
    private String mode;

    @NotBlank
    private String stateMsg;

    @NotNull
    private Integer amount;

    @NotNull
    private Integer discountAmount;

    @NotNull
    private Integer paidAmount;

    @NotBlank
    private String payMethod;

    @NotBlank
    private String orderNo;

    @NotBlank
    private String payToken;

    @NotBlank
    private String transactionId;

    private String cardCompanyName;

    private Integer cardCompanyCode;

    private String cardAuthorizationNo;

    private Integer spreadOut;

    private boolean noInterest;

    private String salesCheckLinkUrl;

    private String cardMethodType;

    private String cardNumber;

    private String cardUserType;

    private String cardBinNumber;

    private String cardNum4Print;

    private String accountBankCode;

    private String accountBankName;

    private String accountNumber;

    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @NotNull
    @Column(columnDefinition = "timestamp")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    public void updateAddress(Address address) {
        this.address = address;
        this.modifiedDate = LocalDateTime.now();
    }

    public void updateDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        this.modifiedDate = LocalDateTime.now();
    }

    public void cardOrder(JsonNode jsonNode) {
        this.cardCompanyName = jsonNode.get("cardCompanyName").asText();
        this.cardCompanyCode = jsonNode.get("cardCompanyCode").asInt();
        this.cardAuthorizationNo = jsonNode.get("cardAuthorizationNo").asText();
        this.spreadOut = jsonNode.get("spreadOut").asInt();
        this.noInterest = jsonNode.get("noInterest").asBoolean();
        this.salesCheckLinkUrl = jsonNode.get("salesCheckLinkUrl").asText();
        this.cardMethodType = jsonNode.get("cardMethodType").asText();
        this.cardNumber = jsonNode.get("cardNumber").asText();
        this.cardUserType = jsonNode.get("cardUserType").asText();
        this.cardBinNumber = jsonNode.get("cardBinNumber").asText();
        this.cardNum4Print = jsonNode.get("cardNum4Print").asText();
    }

    public void tossMoneyOrder(JsonNode jsonNode) {
        this.accountBankCode = jsonNode.get("accountBankCode").asText();
        this.accountBankName = jsonNode.get("accountBankName").asText();
        this.accountNumber = jsonNode.get("accountNumber").asText();
    }

}
