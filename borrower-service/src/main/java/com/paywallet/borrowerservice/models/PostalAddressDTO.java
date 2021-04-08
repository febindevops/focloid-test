package com.paywallet.borrowerservice.models;

import com.paywallet.borrowerservice.entities.enums.AddressTypeEnum;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import lombok.Data;

@Data
public class PostalAddressDTO {
    private AddressTypeEnum addressType;
    private ChoiceList isPrimary;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String zipcode;
    private String state;
    private String country;
}
