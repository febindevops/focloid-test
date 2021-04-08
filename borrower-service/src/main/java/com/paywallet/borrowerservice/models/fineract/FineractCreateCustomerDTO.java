package com.paywallet.borrowerservice.models.fineract;

import java.util.Set;

import lombok.Data;

@Data
public class FineractCreateCustomerDTO {
    String firstname;
    String lastname;
    String externalId;
    String dateFormat; //"dd MMMM yyyy"
    String locale; //en
    Boolean active;
    String activationDate;
    String submittedOnDate;
    long officeId;
    long savingsProductId;
    Set<FineractAddressDTO> address;
}
