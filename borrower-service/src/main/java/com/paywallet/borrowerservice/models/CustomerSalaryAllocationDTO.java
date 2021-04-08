package com.paywallet.borrowerservice.models;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CustomerSalaryAllocationDTO {
    private long argylePayrollCacheId;
    private long salaryAllocationId;
    private long customerId;
    private long walletId;
    private long employerId;
    private String sourceSystem;
    private long tenantId;
    private String salaryRoutingNumber;
    private String salaryAccountNumber;
    private BigDecimal salaryCredited;
    private double salaryPercentAllocatedForWallet;
    private String salaryAggregationAccountId;
    private Date salaryAggregationAllocationTerminationDate;
    private String salaryAcctDebitCardNumber;
    private Date salaryAcctDebitCardExpDate;
    private int salaryAcctDebitCardCVV;
}
