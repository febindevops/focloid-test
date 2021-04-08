package com.paywallet.borrowerservice.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;

import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.entities.aggregators.ArgyleEntity;
import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.aggregators.PinwheelEntity;
import com.paywallet.borrowerservice.entities.customer.FineractCustomerEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEmployerEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.customer.PhoneEntity;
import com.paywallet.borrowerservice.entities.customer.PurchaseAndInstallmentEntity;
import com.paywallet.borrowerservice.models.CustomerProfileDTO;
import com.paywallet.borrowerservice.models.CustomerSalaryAllocationDTO;
import com.paywallet.borrowerservice.models.OTPVerificationDTO;
import com.paywallet.borrowerservice.models.ProspectDTO;
import com.paywallet.borrowerservice.models.argyle.ArgyleConnectDTO;
import com.paywallet.borrowerservice.models.argyle.ArgyleEncConfigModel;
import com.paywallet.borrowerservice.models.argyle.ArgylePayWalletDTO;
import com.paywallet.borrowerservice.models.argyle.ArgyleProspectProfileModel;
import com.paywallet.borrowerservice.models.employers.EmployersListDTO;
import com.paywallet.borrowerservice.models.pinwheel.PinwheelLinkTokensModel;
import com.paywallet.borrowerservice.models.pinwheel.PinwheelProspectProfileModel;
import com.paywallet.borrowerservice.models.purchase.ArgylePayDistributionModel;
import com.paywallet.borrowerservice.services.ArgyleService;
import com.paywallet.borrowerservice.services.OTPVerificationService;
import com.paywallet.borrowerservice.services.PartyService;
import com.paywallet.borrowerservice.services.PayAllocationService;
import com.paywallet.borrowerservice.services.PinwheelService;
import com.paywallet.borrowerservice.services.WalletService;

import org.apache.commons.collections4.map.MultiValueMap;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/borrower")
public class CustomerController {

    @Autowired
    PartyService partyService;

    @Autowired
    ArgyleService argyleService;

    @Autowired
    PinwheelService pinwheelService;

    @Autowired
    PayAllocationService allocationService;

    @Autowired
    WalletService walletServce;

    @Autowired
    private OTPVerificationService otpVerificationService;

    @Value("${argyle.pluginKey}")
    private String argylePluginKey;

    @PostMapping(value = "/create-prospect", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public PartyEntity createProspect(@Valid @RequestBody ProspectDTO prospect) throws Exception {
        return this.partyService.createProspect(prospect);
    }

    @GetMapping("/argyle-connect/{prospectId}")
    public ModelAndView argyleConnect(@PathVariable("prospectId") String prospectId) {
        var parameters = new HashMap<String, String>();
        parameters.put("prospectId", prospectId);
        parameters.put("pluginKey", argylePluginKey);
        String userToken = this.argyleService.getArgyleUserToken(Long.valueOf(prospectId));
        if (userToken == null)
            userToken = "nil";
        parameters.put("userToken", userToken);
        return new ModelAndView("argyle_connect", parameters);
    }

    @PostMapping(value = "/argyle-connect/{prospectId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.OK)
    public ArgyleEntity argyleConnect(@PathVariable("prospectId") long prospectId,
            @Valid @RequestBody ArgyleConnectDTO argyleConnectDTO) {
        return argyleService.connectProspect(prospectId, argyleConnectDTO);
    }

    @GetMapping("/argyle-profile/{prospectId}")
    public ArgyleProspectProfileModel argyleProfile(@PathVariable("prospectId") long prospectId) {
        return argyleService.getUserDetails(prospectId);
    }

    @GetMapping("/employers/{prospectId}")
    public Set<EmployersEntity> getProspectEmployersList(@PathVariable("prospectId") String prospectId) {
        return partyService.getProspectEmployersList(Long.parseLong((prospectId)));
    }

    @PatchMapping("/employers/{prospectId}")
    public Map<String, String> replaceProspectEmployers(@PathVariable("prospectId") String prospectId,
            @Valid @RequestBody EmployersListDTO employerList

    ) {
        return partyService.replaceEmployersInProspect(Long.parseLong((prospectId)), employerList.getEmployers());
    }

    @PutMapping("/employers/{prospectId}/{employerId}")
    public Map<String, String> addEmployerToProspect(@PathVariable("prospectId") String prospectId,
            @PathVariable("employerId") String employerId) {
        return partyService.addEmployerToProspect(Long.parseLong((prospectId)), Long.parseLong((employerId)));
    }

    // NEED TO CHECK THIS!
    @DeleteMapping("/employers/{prospectId}/{employerId}")
    public Map<String, String> removeEmployersToProspect(@PathVariable("prospectId") String prospectId,
            @PathVariable("employerId") String employerId) {
        return partyService.removeProspectEmployer(Long.parseLong((prospectId)), Long.parseLong((employerId)));
    }

    @GetMapping("/allocation")
    public List<ArgylePayWalletDTO> payAllocation(@RequestParam() long customerId) {
        return allocationService.payAllocation(customerId);
    }

    @PostMapping("/allocation")
    public CustomerSalaryAllocationDTO savePayAllocation(
            @RequestBody CustomerSalaryAllocationDTO customerSalaryAllocation) {
        return allocationService.savePayAllocation(customerSalaryAllocation);
    }

    @PostMapping("/profile")
    public PartyEntity createCustomerProfile(@Valid @RequestBody CustomerProfileDTO customerSalaryAllocation) {
        return partyService.createCustomerProfile(customerSalaryAllocation);
    }

    @PostMapping("/wallet/{prospectId}")
    public FineractCustomerEntity createCustomerWallet(@PathVariable("prospectId") String prospectId) {
        return walletServce.createCustomerWallet(Long.parseLong(prospectId));
    }

    @GetMapping("/installments")
    public List<PurchaseAndInstallmentEntity> getInstallments() {
        return partyService.getInstallments();
    }

    @GetMapping("/installments/{installmentId}")
    public PurchaseAndInstallmentEntity getInstallmentsWithInstallmentID(
            @PathVariable("installmentId") String installmentId) {
        return partyService.getInstallmentsWithInstallmentID(Long.parseLong(installmentId));
    }

    // ---------------------------------------------------------------------------------------------------
    // @PostMapping("/argyle-encrypt/{prospectId}")
    // public ArgyleEncConfigModel argyleEncrypt(@PathVariable("prospectId") String
    // prospectId) {
    // return this.argyleService.argyleEncrypt(Long.valueOf(prospectId));
    // }

    // @GetMapping("/argyle-distribution/{prospectId}")
    // public ModelAndView argyleDistribution(@PathVariable("prospectId") String
    // prospectId) {
    // var parameters = new HashMap<String, String>();
    // parameters.put("prospectId", prospectId);
    // parameters.put("pluginKey", argylePluginKey);
    // String userToken =
    // this.argyleService.getArgyleUserToken(Long.valueOf(prospectId));
    // if (userToken == null)
    // userToken = "nil";
    // parameters.put("userToken", userToken);
    // return new ModelAndView("argyle_distribution", parameters);
    // }

    @GetMapping("/argyle-distribution/{prospectId}")
    public ModelAndView argyleDistributionCombined(@PathVariable("prospectId") String prospectId) {
        var parameters = new HashMap<String, String>();
        parameters.put("prospectId", prospectId);
        parameters.put("pluginKey", argylePluginKey);
        String userToken = this.argyleService.getArgyleUserToken(Long.valueOf(prospectId));
        if (userToken == null)
            userToken = "nil";
        parameters.put("userToken", userToken);
        // this.argyleService.argyleEncrypt(Long.valueOf(prospectId));
        return new ModelAndView("argyle_distribution", parameters);
    }

    @PostMapping("otpVerification/{prospectId}")
    public GeneralHttpResponse<String> verifyOtp(@RequestBody OTPVerificationDTO otp,
            @PathVariable("prospectId") long prospectId) {
        return otpVerificationService.verifyOTP(otp, prospectId);
    }

    @GetMapping("/argyle-accounts/{prospectId}/{employerId}")
    @ResponseStatus(HttpStatus.OK)
    public ArgylePayDistributionModel argyleAccountsWithCustomerId(@PathVariable("prospectId") long prospectId,
            @PathVariable("employerId") long employerId) {
        return partyService.argyleAccountsWithCustomerId(prospectId, employerId);
    }

    @GetMapping("/pinwheel-connect/{prospectId}/{employerId}")
    public ModelAndView pinwheelConnect(@PathVariable("prospectId") Integer prospectId,
            @PathVariable("employerId") Integer employerId) {
        var parameters = new HashMap<String, String>();
        PinwheelLinkTokensModel linkToken = pinwheelService.linkToken(employerId, prospectId);
        parameters.put("prospectId", String.valueOf(prospectId));
        parameters.put("token", linkToken.getToken());
        parameters.put("pinWheelId", Long.toString(linkToken.getPinWheelId()));
        return new ModelAndView("pinwheel_connect", parameters);
    }

    @PutMapping("/pinwheel/{pinwheelId}/{accountId}")
    public PinwheelEntity pinwheelUpdate(@PathVariable("pinwheelId") Long pinwheelId,
            @PathVariable("accountId") String accountId) {
        return pinwheelService.pinwheelUpdate(pinwheelId, accountId);
    }

    @GetMapping("/pinwheel-profile/{prospectId}")
    public PinwheelProspectProfileModel pinwheelProfile(@PathVariable("prospectId") long prospectId) {
        return pinwheelService.getUserDetails(prospectId);
    }
}
