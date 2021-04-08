package com.paywallet.borrowerservice.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralHttpResponse<T> {
    private long id;
    private int status;
    private boolean isError;
    private String message;
    private T payload;
}

