package com.paywallet.borrowerservice.controllers;

import javax.validation.Valid;

import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.models.PurchaseAndInstallmentDTO;
import com.paywallet.borrowerservice.services.PurchaseAndInstallmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lender")
public class LenderController {

    @Autowired
    PurchaseAndInstallmentService purchaseAndInstallmentService;

    @PostMapping("/installment/{customerId}")
    public GeneralHttpResponse purchaseAndInstallment(
            @Valid @RequestBody PurchaseAndInstallmentDTO purchaseAndInstallment,
            @PathVariable("customerId") String customerId) {
        return purchaseAndInstallmentService.purchaseAndInstallment(purchaseAndInstallment,
                Long.parseLong((customerId)));
    }
}
