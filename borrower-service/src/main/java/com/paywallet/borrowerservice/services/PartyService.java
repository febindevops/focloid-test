package com.paywallet.borrowerservice.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.constraints.Null;

import com.paywallet.borrowerservice.common.GeneralHttpException;
import com.paywallet.borrowerservice.entities.customer.EmailEntity;
import com.paywallet.borrowerservice.entities.customer.FineractCustomerEntity;
import com.paywallet.borrowerservice.entities.customer.OTPVerificationEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEmployerEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEmployerID;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.customer.PhoneEntity;
import com.paywallet.borrowerservice.entities.customer.PostalAddressEntity;
import com.paywallet.borrowerservice.entities.customer.PurchaseAndInstallmentEntity;
import com.paywallet.borrowerservice.entities.customer.WalletEntity;
import com.paywallet.borrowerservice.entities.enums.AccountRoleEnum;
import com.paywallet.borrowerservice.entities.enums.AccountTypeEnum;
import com.paywallet.borrowerservice.entities.enums.AggregatorTypeEnum;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;
import com.paywallet.borrowerservice.entities.enums.PartyRoleEnum;
import com.paywallet.borrowerservice.models.CustomerProfileDTO;
import com.paywallet.borrowerservice.models.ProspectDTO;
import com.paywallet.borrowerservice.models.fineract.FineractAddressDTO;
import com.paywallet.borrowerservice.models.fineract.FineractCreateCustomerDTO;
import com.paywallet.borrowerservice.models.purchase.ArgylePayDistributionModel;
import com.paywallet.borrowerservice.repositories.ArgyleRepository;
import com.paywallet.borrowerservice.entities.enums.AggregatorTypeEnum;
import com.paywallet.borrowerservice.repositories.EmailRepository;
import com.paywallet.borrowerservice.repositories.FineractCustomerRepository;
import com.paywallet.borrowerservice.repositories.OTPVerificationRepository;
import com.paywallet.borrowerservice.repositories.PartyEmployerRepository;
import com.paywallet.borrowerservice.repositories.PartyRepository;
import com.paywallet.borrowerservice.repositories.PhoneRepository;
import com.paywallet.borrowerservice.repositories.PostalAddressRepository;
import com.paywallet.borrowerservice.repositories.WalletRepository;
import com.paywallet.borrowerservice.entities.aggregators.ArgyleEntity;
import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

@Service
public class PartyService {
    private static final Logger logger = LogManager.getLogger(ArgyleService.class);

    @Autowired
    private EntityManager em;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private PurchaseAndInstallmentService purchaseAndInstallmentService;

    @Autowired
    private FineractService fineractService;

    @Autowired
    private FineractCustomerRepository fineractCustomerRepository;

    @Autowired
    private OTPVerificationRepository otpVerificationRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ArgyleService argyleService;

    @Autowired
    private PartyEmployerRepository partyEmployerRepository;

    @Autowired
    EmployerService empService;

    public PartyEntity createProspect(ProspectDTO prospect) throws Exception {
        if (this.emailRepository.findByEmailId(prospect.getEmail()) != null) {
            throw new GeneralHttpException("error", "Email already exists !");
        }
        if (this.phoneRepository.findByPhoneNum(prospect.getPhone()) != null) {
            throw new GeneralHttpException("error", "Phone already exists !");
        }
        PartyEntity party = new PartyEntity();
        party.setFirstName(prospect.getFirstName());
        party.setMiddleName(prospect.getMiddleName());
        party.setLastName(prospect.getLastName());
        party.setPartyRole(PartyRoleEnum.PROSPECT);
        PartyEntity result = this.partyRepository.save(party);

        EmailEntity emailEntity = new EmailEntity();
        emailEntity.setIsPrimary(ChoiceList.YES);
        emailEntity.setEmailId(prospect.getEmail());
        emailEntity.setParty(party);
        emailEntity.setEmailType("PRIMARY");
        this.emailRepository.save(emailEntity);

        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setIsPrimary(ChoiceList.YES);
        phoneEntity.setPhoneNum(prospect.getPhone());
        phoneEntity.setParty(party);
        this.phoneRepository.save(phoneEntity);

        // Generate OTP for verification
        otpVerificationRepository.save(new OTPVerificationEntity(party, 1234, "SMS", new Date()));
        return result;
        // fineractService
    }

    public Optional<PartyEntity> getByCustomerId(long customerId) {
        return this.partyRepository.findById(customerId);
    }

    public void updateConnectedAggregator(long customerId, AggregatorTypeEnum aggregator) {
        Optional<PartyEntity> optionalParty = this.partyRepository.findById(customerId);
        if (optionalParty.isPresent()) {
            PartyEntity party = optionalParty.get();
            party.setAggregatorConnected(aggregator);
            this.partyRepository.save(party);
        } else {
            throw new GeneralHttpException("error", "Customer not found !");
        }
    }

    @Transactional
    public Map<String, String> replaceEmployersInProspect(Long prospectId, List<Long> employers) {
        Optional<PartyEntity> optionalParty = this.partyRepository.findById(prospectId);
        if (optionalParty.isPresent()) {
            PartyEntity party = optionalParty.get();
            party.getEmployers().clear();
            employers.forEach(employerId -> {
                EmployersEntity employer = this.em.getReference(EmployersEntity.class, employerId);
                employer.setEmployerPrimaryId(employerId);
                PartyEmployerEntity partyEmployer = new PartyEmployerEntity();
                employer.setEmployerPrimaryId(employerId);
                partyEmployer.setEmployer(employer);
                partyEmployer.setParty(party);
                party.getEmployers().add(partyEmployer);
            });
            // this.em.
            PartyEntity result = this.em.merge(party);
            this.em.flush();
            Map<String, String> response = new HashMap<>();
            response.put("status", "completed");
            return response;
        } else
            throw new GeneralHttpException("error", "Prospect not found !");
    }

    public Set<EmployersEntity> getProspectEmployersList(Long prospectId) {
        Optional<PartyEntity> customer = partyRepository.findById(prospectId);
        Set<PartyEmployerEntity> partyEmployer = partyEmployerRepository.findAllByParty(customer.get());
        Set<EmployersEntity> employers = new HashSet<EmployersEntity>();
        if (partyEmployer != null) {
            for (PartyEmployerEntity employer : partyEmployer) {
                employers.add(employer.getEmployer());
            }
            return employers;
        } else {
            throw new GeneralHttpException("error", "Customer not found !");
        }
    }

    @Transactional
    public Map<String, String> addEmployerToProspect(Long prospectId, Long employerId) {
        Optional<PartyEntity> optionalParty = this.partyRepository.findById(prospectId);
        if (optionalParty.isPresent()) {
            PartyEntity party = optionalParty.get();
            PartyEmployerEntity partyEmployer = new PartyEmployerEntity();
            EmployersEntity employer = this.em.getReference(EmployersEntity.class, employerId);
            employer.setEmployerPrimaryId(employerId);
            partyEmployer.setEmployer(employer);
            partyEmployer.setParty(party);
            try {
                party.getEmployers().add(partyEmployer);
                this.em.merge(party);
                this.em.flush();
            } catch (IllegalStateException e) {
                throw new GeneralHttpException("error", "Already connected this prospect to the employer");
            }
            Map<String, String> response = new HashMap<>();
            response.put("status", "completed");
            return response;
        } else
            throw new GeneralHttpException("error", "Prospect not found !");
    }

    @Transactional
    public Map<String, String> removeProspectEmployer(long prospectId, long employerId) {
        Optional<PartyEntity> optionalParty = this.partyRepository.findById(prospectId);
        if (optionalParty.isPresent()) {
            PartyEntity party = optionalParty.get();
            EmployersEntity employer = this.em.getReference(EmployersEntity.class, employerId);
            employer.setEmployerPrimaryId(employerId);
            PartyEmployerEntity partyEmployer = new PartyEmployerEntity();
            employer.setEmployerPrimaryId(employerId);
            partyEmployer.setEmployer(employer);
            partyEmployer.setParty(party);
            party.getEmployers().add(partyEmployer);
            party.getEmployers().removeIf(filter -> filter.getEmployer().getEmployerPrimaryId() == employerId);
            this.em.merge(party);
            this.em.flush();
            Map<String, String> response = new HashMap<>();
            response.put("status", "completed");
            return response;
        } else
            throw new GeneralHttpException("error", "Prospect not found !");
    }

    public PartyEntity createCustomerProfile(CustomerProfileDTO customerSalaryAllocation) {
        Optional<PartyEntity> partyOpt = this.partyRepository.findById(customerSalaryAllocation.getCustomerId());
        if (!partyOpt.isPresent()) {
            throw new GeneralHttpException("error", "Prospect doesn't exist");
        }
        PartyEntity party = partyOpt.get();
        party.setSourceSystem(customerSalaryAllocation.getSourceSystem());
        party.setTenantId(customerSalaryAllocation.getTenantId());
        party.setSuffix(customerSalaryAllocation.getSuffix());
        party.setPrefix(customerSalaryAllocation.getPrefix());
        party.setEntityName(customerSalaryAllocation.getEntityName());
        party.setDateOfBirth(customerSalaryAllocation.getDateOfBirth());
        party.setIdentificationNumber(customerSalaryAllocation.getIdentificationNumber());
        party.setIdentificationType(customerSalaryAllocation.getIdentificationType());
        // party.setPartyRole(PartyRoleEnum.BORROWER);
        PartyEntity partyRes = this.partyRepository.save(party);
        PostalAddressEntity postalAddress = new PostalAddressEntity();
        postalAddress.setAddressType(customerSalaryAllocation.getAddress().getAddressType());
        postalAddress.setIsPrimary(ChoiceList.YES);
        postalAddress.setAddress1(customerSalaryAllocation.getAddress().getAddress1());
        postalAddress.setAddress2(customerSalaryAllocation.getAddress().getAddress2());
        postalAddress.setAddress3(customerSalaryAllocation.getAddress().getAddress3());
        postalAddress.setCity(customerSalaryAllocation.getAddress().getCity());
        postalAddress.setZipcode(customerSalaryAllocation.getAddress().getZipcode());
        postalAddress.setState(customerSalaryAllocation.getAddress().getState());
        postalAddress.setCountry(customerSalaryAllocation.getAddress().getCountry());
        postalAddress.setParty(partyRes);
        PostalAddressEntity postalRes = postalAddressRepository.save(postalAddress);
        Set<FineractAddressDTO> finAddressSet = new HashSet<FineractAddressDTO>();
        FineractAddressDTO finAddress = new FineractAddressDTO();
        finAddress.setAddressTypeId("1");
        finAddress.setIsActive(true);
        finAddress.setStreet(postalRes.getCity()); // street?
        finAddress.setStateProvinceId(18);
        finAddress.setCountryId(19);
        finAddressSet.add(finAddress);
        return party;
    }

    public List<PurchaseAndInstallmentEntity> getInstallments() {
        return purchaseAndInstallmentService.getInstallments();
    }

    public PurchaseAndInstallmentEntity getInstallmentsWithInstallmentID(long installmentID) {
        Optional<PurchaseAndInstallmentEntity> purchaseAndinstallment = purchaseAndInstallmentService
                .getInstallmentsWithInstallmentID(installmentID);
        return purchaseAndinstallment.get();
    }

    public PhoneEntity getCustomerFromPhoneNumber(String phoneNumber) {
        return phoneRepository.findByPhoneNum(phoneNumber);
    }

    public Set<String> getArgyleEmployeesFromCustomerID(long customerId) {
        PartyEntity customer = partyRepository.findByCustomerId(customerId);
        Set<PartyEmployerEntity> employers = partyEmployerRepository.findAllByParty(customer);

        Set<String> employerSet = new HashSet<String>();
        employers.stream().forEach((PartyEmployerEntity employer) -> {
            employerSet.add(Long.toString(employer.getEmployer().getEmployerPrimaryId()) + "-"
                    + employer.getEmployer().getName());
        });
        return employerSet;
    }

    public ArgylePayDistributionModel argyleAccountsWithCustomerId(long prospectId, long employerId) {
        ArgyleEntity argyle = argyleService.getArgyleByParty(prospectId);
        JSONArray accounts = argyleService.getAccountsById(argyle.getUserId()).getJSONArray("results");
        String argyleID = empService.getEmployersWithID(employerId);
        ArgylePayDistributionModel payDist = new ArgylePayDistributionModel();
        for (int i = 0; i < accounts.length(); i++) {
            JSONObject payDIstObj = accounts.getJSONObject(i);
            if (payDIstObj.getString("data_partner").equalsIgnoreCase(argyleID)) {
                payDist.setAccountId(payDIstObj.getString("id"));
                payDist.setUser(payDIstObj.getString("user"));
                payDist.setStatus(payDIstObj.getString("status"));
                payDist.setLinkItem(payDIstObj.getString("link_item"));
                payDist.setSource(payDIstObj.getString("source"));
                payDist.setPayDistributionStatus(payDIstObj.getJSONObject("pay_distribution").getString("status"));
                break;
            }
        }
        return payDist;
    }

    public PartyEntity saveParty(PartyEntity party) {
        return partyRepository.save(party);
    }

}
