package com.paywallet.borrowerservice.models;

import lombok.Data;

@Data
public class BankAccountModel {
    private String routingNumber;
    private String accountNumber;
    private String accountType;
}
