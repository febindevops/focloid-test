package com.paywallet.borrowerservice.models;

import java.util.Date;

import com.paywallet.borrowerservice.entities.enums.IdentificationTypeEnum;

import lombok.Data;

@Data
public class CustomerProfileDTO {
    private long customerId;
    private String sourceSystem;
    private long tenantId;
    private String suffix;
    private String prefix;
    private String entityName;
    private Date dateOfBirth;
    private String identificationNumber;
    private IdentificationTypeEnum identificationType;// SSN,Drivers License,Passport
    private PostalAddressDTO address;
}