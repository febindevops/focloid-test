package com.paywallet.borrowerservice.models;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PurchaseAndInstallmentDTO {
    private String itemPurchased;
    private String purchasedFrom;
    private BigDecimal purchaseAmount;
    private String currency;
    private BigDecimal taxAmount;
    private BigDecimal deliveryCharges;
    private BigDecimal totalCost;
    private long installmentMinNo;
    private long installmentMaxNo;
    private Date firstInstallmentDate;
    private Date lastInstallmentDate;
}
