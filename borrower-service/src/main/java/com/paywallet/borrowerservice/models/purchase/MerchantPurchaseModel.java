package com.paywallet.borrowerservice.models.purchase;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MerchantPurchaseModel {
    String itemPurchased;
    String purchasedFrom;
    BigDecimal purchaseAmount;
    BigDecimal tax;
    BigDecimal deiveryChargers;
    BigDecimal installationCharges;
    BigDecimal totalCost;
    long tenure;
    BigDecimal firstInstallment;
    BigDecimal lastInstallment;
    Date firstInstallmentDate;
    Date lastInstallmentDate;
    String redirectUrl;
}