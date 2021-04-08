package com.paywallet.borrowerservice.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.customer.PhoneEntity;
import com.paywallet.borrowerservice.entities.customer.WalletEntity;
import com.paywallet.borrowerservice.models.argyle.ArgyleEncConfigModel;
import com.paywallet.borrowerservice.models.purchase.MerchantPurchaseModel;
import com.paywallet.borrowerservice.services.ArgyleService;
import com.paywallet.borrowerservice.services.PartyService;
import com.paywallet.borrowerservice.services.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Value("${argyle.pluginKey}")
    private String argylePluginKey;

    @Autowired
    PartyService partyService;

    @Autowired
    WalletService walletService;

    @Autowired
    ArgyleService argyleService;

    @PostMapping(value = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ModelAndView borrowerLogin(@RequestParam Map<String, String> loginDetails) {
        return new ModelAndView("purchase_login", loginDetails);
    }

    @PostMapping(value = "/details", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ModelAndView borrowerLoginDetails(@RequestParam Map<String, String> loginDetails) {
        PhoneEntity phone = partyService.getCustomerFromPhoneNumber(loginDetails.get("username"));
        long customerId = phone.getParty().getCustomerId();
        loginDetails.put("customerId", Long.toString(customerId));
        Set<String> employers = partyService.getArgyleEmployeesFromCustomerID(customerId);
        loginDetails.put("employers", employers.toString());
        return new ModelAndView("purchase_login_details", loginDetails);
    }

    @PostMapping(value = "/argyle-distribution/{prospectId}", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public ModelAndView argyleDistributionCombined(@PathVariable("prospectId") String prospectId,
            @RequestParam Map<String, String> purchaseDetails) {
        purchaseDetails.put("prospectId", prospectId);
        purchaseDetails.put("pluginKey", argylePluginKey);
        String employerID = purchaseDetails.get("employer").split("-")[0];
        WalletEntity wallet = walletService.getSalaryCollectionAccount(Long.parseLong(prospectId));
        BigDecimal maxAmount = BigDecimal.valueOf(Math.max(Double.parseDouble(purchaseDetails.get("firstInstallment")),
                Double.parseDouble(purchaseDetails.get("lastInstallment"))));
        ArgyleEncConfigModel argyleConfig = argyleService.argyleEncrypt(Long.valueOf(prospectId),
                wallet.formattedAccountNumber(), maxAmount);
        String userToken = this.argyleService.getArgyleUserToken(Long.valueOf(prospectId));
        if (userToken == null)
            userToken = "nil";
        purchaseDetails.put("userToken", userToken);
        purchaseDetails.put("argyleConfig", argyleConfig.getData());
        purchaseDetails.put("employerID", employerID);
        purchaseDetails.put("walletID", wallet.formattedWalletId());
        purchaseDetails.put("accountID", wallet.formattedAccountNumber());
        return new ModelAndView("argyle_distribution", purchaseDetails);
    }

    @PostMapping("/argyle_verify/{customerId}/{employerId}")
    public ModelAndView argyleVerify(@PathVariable("customerId") String customerId,
            @PathVariable("employerId") String employerId, @RequestParam Map<String, String> verifyData) {
        verifyData.put("customerId", customerId);
        verifyData.put("employerId", employerId);

        return new ModelAndView("argyle_verify", verifyData);
    }

}
