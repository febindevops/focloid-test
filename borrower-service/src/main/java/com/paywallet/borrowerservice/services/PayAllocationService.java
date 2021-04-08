package com.paywallet.borrowerservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.paywallet.borrowerservice.common.GeneralHttpException;
import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.customer.ArgylePayrollCacheEntity;
import com.paywallet.borrowerservice.entities.customer.CustomerSalaryAllocationEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.models.BankAccountModel;
import com.paywallet.borrowerservice.models.CustomerSalaryAllocationDTO;
import com.paywallet.borrowerservice.models.argyle.ArgylePayWalletDTO;
import com.paywallet.borrowerservice.repositories.CustomerSalayAllocationRepository;
import com.paywallet.borrowerservice.repositories.PayAllocationRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

@Service
public class PayAllocationService {

    private static final Logger logger = LogManager.getLogger(PayAllocationService.class);

    private String error = "error";

    @Autowired
    private ArgyleService argyleService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private PayAllocationRepository payAllocationRepository;

    @Autowired
    private CustomerSalayAllocationRepository customerSalaryAllocationRepository;

    public List<ArgylePayWalletDTO> payAllocation(long customerId) {
        String argyleUserID = argyleService.getArgyleUserId(customerId);
        JSONObject argylePayAllocResult = argyleService.getArgylePayAllocation(argyleUserID);
        logger.info(argylePayAllocResult);
        JSONArray argylePayAllocation = argylePayAllocResult.getJSONArray("results");
        List<ArgylePayWalletDTO> argylePaywallet = new ArrayList<ArgylePayWalletDTO>();

        if (argylePayAllocation.length() > 0) {
            for (int i = 0; i < argylePayAllocation.length(); i++) {
                JSONObject alloc = argylePayAllocation.getJSONObject(i);
                if (alloc.getString("allocation_value").equalsIgnoreCase("remainder")) {

                    ArgylePayrollCacheEntity payrollCache = payAllocationRepository
                            .findByAccountId(alloc.getString("account"));
                    ArgylePayrollCacheEntity payAlloc;
                    if (payrollCache == null) {
                        ArgylePayrollCacheEntity argylePayrollCache = new ArgylePayrollCacheEntity();
                        argylePayrollCache.setPayAllocationId(alloc.getString("id"));
                        argylePayrollCache.setAccountId(alloc.getString("account"));
                        JSONObject bankAccount = alloc.getJSONObject("bank_account");
                        argylePayrollCache.setRoutingNumber(bankAccount.getString("routing_number"));
                        argylePayrollCache.setAccountNumber(bankAccount.getString("account_number"));
                        argylePayrollCache.setAccounttype(bankAccount.getString("account_type"));
                        argylePayrollCache.setAllocationType(alloc.getString("allocation_type"));
                        argylePayrollCache.setEmployer(alloc.getString("employer"));
                        argylePayrollCache.setProspectId(customerId);
                        payAlloc = payAllocationRepository.save(argylePayrollCache);
                    } else {
                        payrollCache.setPayAllocationId(alloc.getString("id"));
                        JSONObject bankAccount = alloc.getJSONObject("bank_account");
                        payrollCache.setRoutingNumber(bankAccount.getString("routing_number"));
                        payrollCache.setAccountNumber(bankAccount.getString("account_number"));
                        payrollCache.setAccounttype(bankAccount.getString("account_type"));
                        payrollCache.setAllocationType(alloc.getString("allocation_type"));
                        payrollCache.setEmployer(alloc.getString("employer"));
                        payrollCache.setProspectId(customerId);
                        payAlloc = payAllocationRepository.save(payrollCache);
                    }
                    ArgylePayWalletDTO wallet = this.setToReturnDTO(payAlloc);
                    argylePaywallet.add(wallet);
                }
            }
        } else {
            throw new GeneralHttpException(this.error, "No pay wallet allocations found !");
        }

        return argylePaywallet;
    }

    public CustomerSalaryAllocationDTO savePayAllocation(CustomerSalaryAllocationDTO customerSalaryAllocation) {

        // get data from payallocationrepo and compare with the last 4
        boolean checkPass = this.checkAccountandRouting(customerSalaryAllocation);
        CustomerSalaryAllocationDTO returnDTO = new CustomerSalaryAllocationDTO();
        if (checkPass) {

            CustomerSalaryAllocationEntity payrollCache = customerSalaryAllocationRepository
                    .findBySalaryAccountNumberAndSalaryAggregationAccountId(
                            customerSalaryAllocation.getSalaryAccountNumber(),
                            customerSalaryAllocation.getSalaryAggregationAccountId());
            CustomerSalaryAllocationEntity ret;
            if (payrollCache == null) {
                CustomerSalaryAllocationEntity custSalaryAlloc = new CustomerSalaryAllocationEntity();
                PartyEntity party = new PartyEntity();
                party.setCustomerId(customerSalaryAllocation.getCustomerId());
                EmployersEntity employer = new EmployersEntity();
                employer.setEmployerPrimaryId(customerSalaryAllocation.getEmployerId());
                custSalaryAlloc.setCustomer(party);
                custSalaryAlloc.setWalletId(customerSalaryAllocation.getWalletId());
                custSalaryAlloc.setEmployer(employer);
                custSalaryAlloc.setSourceSystem(customerSalaryAllocation.getSourceSystem());
                custSalaryAlloc.setTenantId(customerSalaryAllocation.getTenantId());
                custSalaryAlloc.setSalaryRoutingNumber(customerSalaryAllocation.getSalaryRoutingNumber());
                custSalaryAlloc.setSalaryAccountNumber(customerSalaryAllocation.getSalaryAccountNumber());
                custSalaryAlloc.setSalaryCredited(customerSalaryAllocation.getSalaryCredited());

                custSalaryAlloc.setSalaryAcctDebitCardNumber(customerSalaryAllocation.getSalaryAcctDebitCardNumber());
                custSalaryAlloc.setSalaryAcctDebitCardExpDate(customerSalaryAllocation.getSalaryAcctDebitCardExpDate());
                custSalaryAlloc.setSalaryAcctDebitCardCVV(customerSalaryAllocation.getSalaryAcctDebitCardCVV());

                custSalaryAlloc.setSalaryPercentAllocatedForWallet(
                        customerSalaryAllocation.getSalaryPercentAllocatedForWallet());
                custSalaryAlloc.setSalaryAggregationAccountId(customerSalaryAllocation.getSalaryAggregationAccountId());
                custSalaryAlloc.setSalaryAggregationAllocationTerminationDate(
                        customerSalaryAllocation.getSalaryAggregationAllocationTerminationDate());
                ret = customerSalaryAllocationRepository.save(custSalaryAlloc);
            } else {
                PartyEntity party = new PartyEntity();
                party.setCustomerId(customerSalaryAllocation.getCustomerId());
                EmployersEntity employer = new EmployersEntity();
                employer.setEmployerPrimaryId(customerSalaryAllocation.getEmployerId());
                payrollCache.setCustomer(party);
                payrollCache.setWalletId(customerSalaryAllocation.getWalletId());
                payrollCache.setEmployer(employer);
                payrollCache.setSourceSystem(customerSalaryAllocation.getSourceSystem());
                payrollCache.setTenantId(customerSalaryAllocation.getTenantId());
                payrollCache.setSalaryRoutingNumber(customerSalaryAllocation.getSalaryRoutingNumber());
                payrollCache.setSalaryAccountNumber(customerSalaryAllocation.getSalaryAccountNumber());
                payrollCache.setSalaryCredited(customerSalaryAllocation.getSalaryCredited());
                payrollCache.setSalaryPercentAllocatedForWallet(
                        customerSalaryAllocation.getSalaryPercentAllocatedForWallet());
                payrollCache.setSalaryAggregationAccountId(customerSalaryAllocation.getSalaryAggregationAccountId());
                payrollCache.setSalaryAggregationAllocationTerminationDate(
                        customerSalaryAllocation.getSalaryAggregationAllocationTerminationDate());
                payrollCache.setSalaryAcctDebitCardNumber(customerSalaryAllocation.getSalaryAcctDebitCardNumber());
                payrollCache.setSalaryAcctDebitCardExpDate(customerSalaryAllocation.getSalaryAcctDebitCardExpDate());
                payrollCache.setSalaryAcctDebitCardCVV(customerSalaryAllocation.getSalaryAcctDebitCardCVV());
                ret = customerSalaryAllocationRepository.save(payrollCache);
            }

            BeanUtils.copyProperties(ret, returnDTO, "customer", "employer");
            returnDTO.setCustomerId(ret.getCustomer().getCustomerId());
            returnDTO.setEmployerId(ret.getEmployer().getEmployerPrimaryId());
            returnDTO.setSalaryAllocationId(ret.getCustomerSalaryAllocationId());
            returnDTO.setArgylePayrollCacheId(customerSalaryAllocation.getArgylePayrollCacheId());
        }
        return returnDTO;
    }

    private boolean checkAccountandRouting(CustomerSalaryAllocationDTO customerSalaryAllocation) {
        Optional<ArgylePayrollCacheEntity> payrollCache = payAllocationRepository
                .findById(customerSalaryAllocation.getArgylePayrollCacheId());
        if (payrollCache.isPresent()) {
            String storedAccount = payrollCache.get().getAccountNumber()
                    .substring(payrollCache.get().getAccountNumber().length() - 4);
            String receivedAccount = customerSalaryAllocation.getSalaryAccountNumber()
                    .substring(customerSalaryAllocation.getSalaryAccountNumber().length() - 4);
            if (!storedAccount.equals(receivedAccount)) {
                throw new GeneralHttpException(this.error,
                        "The received account number doesn't match the stored account!");
            }
            String storedRoutingNumber = payrollCache.get().getRoutingNumber()
                    .substring(payrollCache.get().getRoutingNumber().length() - 4);
            String receivedRoutingNumber = customerSalaryAllocation.getSalaryRoutingNumber()
                    .substring(customerSalaryAllocation.getSalaryRoutingNumber().length() - 4);
            if (!storedRoutingNumber.equals(receivedRoutingNumber)) {
                throw new GeneralHttpException(this.error,
                        "The received routing number doesn't match the stored account!");
            }
        } else {
            throw new GeneralHttpException(this.error, "The received payroll cache entry not found!");
        }
        return true;
    }

    private ArgylePayWalletDTO setToReturnDTO(ArgylePayrollCacheEntity payAlloc) {
        EmployersEntity employerDetails = employerService.getEmployerByArgyleId(payAlloc.getEmployer());
        ArgylePayWalletDTO wallet = new ArgylePayWalletDTO();
        wallet.setArgylePayrollCacheId(payAlloc.getArgylePayrollCacheId());
        wallet.setProspectId(payAlloc.getProspectId());
        wallet.setPayAllocationId(payAlloc.getPayAllocationId());
        wallet.setAccountId(payAlloc.getAccountId());
        BankAccountModel bankAcc = new BankAccountModel();
        bankAcc.setAccountNumber(payAlloc.getAccountNumber());
        bankAcc.setRoutingNumber(payAlloc.getRoutingNumber());
        bankAcc.setAccountType(payAlloc.getAccounttype());
        wallet.setBankAccount(bankAcc);
        wallet.setAllocationType(payAlloc.getAllocationType());
        wallet.setUpdatedAt(payAlloc.getLastUpdatedDate());
        wallet.setEmployer(employerDetails);
        return wallet;
    }

}
