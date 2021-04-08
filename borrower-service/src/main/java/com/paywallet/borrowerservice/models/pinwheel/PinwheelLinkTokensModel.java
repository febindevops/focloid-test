package com.paywallet.borrowerservice.models.pinwheel;

import lombok.Data;

@Data
public class PinwheelLinkTokensModel {
    String mode;
    Long expires;
    String tokenId;
    String id;
    Long pinWheelId;
    String token;
}