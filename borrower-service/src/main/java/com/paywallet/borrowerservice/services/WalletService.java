package com.paywallet.borrowerservice.services;

import javax.persistence.EntityManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.paywallet.borrowerservice.common.GeneralHttpException;
import com.paywallet.borrowerservice.entities.customer.FineractCustomerEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.customer.PostalAddressEntity;
import com.paywallet.borrowerservice.entities.customer.WalletEntity;
import com.paywallet.borrowerservice.entities.enums.AccountRoleEnum;
import com.paywallet.borrowerservice.entities.enums.AccountTypeEnum;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;
import com.paywallet.borrowerservice.entities.enums.PartyRoleEnum;
import com.paywallet.borrowerservice.models.fineract.FineractCreateCustomerDTO;
import com.paywallet.borrowerservice.repositories.FineractCustomerRepository;
import com.paywallet.borrowerservice.repositories.PostalAddressRepository;
import com.paywallet.borrowerservice.repositories.WalletRepository;
import com.paywallet.borrowerservice.models.fineract.FineractAddressDTO;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private FineractCustomerRepository fineractCustomerRepository;

    @Autowired
    private FineractService fineractService;

    @Autowired
    private EntityManager em;

    public WalletEntity getSalaryCollectionAccount(long customerId) {
        PartyEntity customer = this.em.getReference(PartyEntity.class, customerId);
        WalletEntity we = this.walletRepository.findByAccountTypeAndCustomer(customer,
                AccountTypeEnum.SALARY_COLLECTION_ACCT);
        if (we == null) {
            throw new GeneralHttpException("error", "Wallet not found !");
        }
        return we;
    }

    public FineractCustomerEntity createCustomerWallet(long prospectId) {
        Optional<PartyEntity> partyOpt = partyService.getByCustomerId(prospectId);

        if (!partyOpt.isPresent()) {
            throw new GeneralHttpException("error", "Prospect doesn't exist");
        }
        PartyEntity party = partyOpt.get();
        if (party.getPartyRole() != PartyRoleEnum.PROSPECT) {
            throw new GeneralHttpException("error", "Not a prospect");
        }
        FineractCustomerEntity fineractRes = new FineractCustomerEntity();
        try {
            FineractCreateCustomerDTO customerDTO = new FineractCreateCustomerDTO();
            customerDTO.setFirstname(party.getFirstName());
            customerDTO.setLastname(party.getLastName());
            customerDTO.setExternalId(Long.toString(party.getCustomerId()));
            Date today = new Date();
            customerDTO.setActivationDate(new SimpleDateFormat("dd MMMM yyyy").format(today));
            customerDTO.setSubmittedOnDate(new SimpleDateFormat("dd MMMM yyyy").format(today));
            customerDTO.setActive(true);
            customerDTO.setOfficeId(1);
            customerDTO.setSavingsProductId(1);
            Set<FineractAddressDTO> finAddressSet = new HashSet<FineractAddressDTO>();
            FineractAddressDTO finAddress = new FineractAddressDTO();
            finAddress.setAddressTypeId("1");
            finAddress.setIsActive(true);
            PostalAddressEntity postalRes = postalAddressRepository.findByPartyAndIsPrimary(party, ChoiceList.YES);
            finAddress.setStreet(postalRes.getCity()); // street?
            finAddress.setStateProvinceId(18);
            finAddress.setCountryId(19);
            finAddressSet.add(finAddress);
            customerDTO.setAddress(finAddressSet);
            JSONObject finRet = fineractService.createCustomer(customerDTO);
            FineractCustomerEntity fineractCust = new FineractCustomerEntity();
            Optional<PartyEntity> partyEn = partyService.getByCustomerId(prospectId);
            fineractCust.setParty(partyEn.get());
            fineractCust.setOfficeId(finRet.getLong("officeId"));
            fineractCust.setClientId(finRet.getLong("clientId"));
            fineractCust.setSavingsId(finRet.getLong("savingsId"));
            fineractCust.setResourceId(finRet.getLong("resourceId"));
            fineractRes = fineractCustomerRepository.save(fineractCust);
            WalletEntity wallet = new WalletEntity();
            wallet.setCustomer(party);
            wallet.setAccountId(finRet.getLong("savingsId"));
            wallet.setAccountType(AccountTypeEnum.SALARY_COLLECTION_ACCT);
            wallet.setAccountRole(AccountRoleEnum.COLLECT_SALARY);
            walletRepository.save(wallet);
            party.setPartyRole(PartyRoleEnum.BORROWER);
            this.partyService.saveParty(party);
        } catch (ResourceAccessException certificateException) {
            throw new GeneralHttpException("error", "Unable to find valid certification path to requested target");
        } catch (GeneralHttpException ex) {
            throw new GeneralHttpException("error", "Wallet creation failed");
        }
        return fineractRes;
    }

}
