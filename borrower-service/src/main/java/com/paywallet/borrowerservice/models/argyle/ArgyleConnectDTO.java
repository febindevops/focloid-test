package com.paywallet.borrowerservice.models.argyle;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ArgyleConnectDTO {

    @NotBlank(message = "Argyle user ID is required !")
    private String userId;
    @NotBlank(message = "Argyle user token is required !")
    private String userToken;


}
