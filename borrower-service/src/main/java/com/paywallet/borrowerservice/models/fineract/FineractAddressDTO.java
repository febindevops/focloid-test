package com.paywallet.borrowerservice.models.fineract;

import lombok.Data;

@Data
public class FineractAddressDTO {
      String addressTypeId;
      Boolean isActive;
      String street;
      long stateProvinceId;
      long countryId;
}
