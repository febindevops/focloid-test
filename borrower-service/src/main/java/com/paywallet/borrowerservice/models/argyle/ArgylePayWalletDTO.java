package com.paywallet.borrowerservice.models.argyle;

import java.util.Date;

import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.models.BankAccountModel;

import lombok.Data;

@Data
public class ArgylePayWalletDTO {
    private long argylePayrollCacheId;
    private long prospectId;
    private String payAllocationId;
    private String accountId;
    private BankAccountModel bankAccount;
    private String allocationType;
    private EmployersEntity employer;
    private Date updatedAt;
}