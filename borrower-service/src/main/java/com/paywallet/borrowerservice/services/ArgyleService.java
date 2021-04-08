package com.paywallet.borrowerservice.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.text.DateFormatter;
import javax.validation.Valid;

import com.paywallet.borrowerservice.common.GeneralHttpException;
import com.paywallet.borrowerservice.entities.aggregators.ArgyleEntity;
import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.enums.AggregatorTypeEnum;
import com.paywallet.borrowerservice.models.argyle.ArgyleConnectDTO;
import com.paywallet.borrowerservice.models.argyle.ArgyleEncConfigModel;
import com.paywallet.borrowerservice.models.argyle.ArgyleProspectProfileModel;
import com.paywallet.borrowerservice.models.employers.EmploymentModel;
import com.paywallet.borrowerservice.repositories.ArgyleRepository;
import com.paywallet.borrowerservice.repositories.EmployersRepository;
import com.paywallet.borrowerservice.repositories.PartyRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ArgyleService {

    private static final Logger logger = LogManager.getLogger(ArgyleService.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ArgyleRepository argyleRepository;

    @Autowired
    PartyService partyService;

    @Autowired
    EmployersRepository employersRepository;

    @Value("${argyle.token}")
    private String argyleToken;

    @Value("${argyle.base}")
    private String argyleBasePath;

    public ArgyleEncConfigModel argyleEncrypt(long prospectId, String accountNumber, BigDecimal amount) {

        // String formatted = String.format("%03d", num);

        JSONObject bankDetails = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        bankDetails.put("bank_name", "Pay-Wallet");
        bankDetails.put("account_type", "checking");
        bankDetails.put("routing_number", "12345678");
        bankDetails.put("account_number", accountNumber);
        JSONObject alloc = new JSONObject();
        alloc.put("value", String.valueOf(amount));
        jsonObject.put("amount_allocation", alloc);
        jsonObject.put("bank_account", bankDetails);

        JSONObject ec = this.postFromArgyle("pay-distribution-config/encrypt", jsonObject);
        if (ec == null)
            throw new GeneralHttpException("error", "Argyle Data Encryption Failed !");
        ArgyleEncConfigModel result = new ArgyleEncConfigModel();
        result.data = ec.getString("encrypted_config");
        return result;
    }

    public String getArgyleUserToken(long prospectId) {
        PartyEntity partyEntity = new PartyEntity();
        partyEntity.setCustomerId(prospectId);
        ArgyleEntity argyleEntity = this.argyleRepository.findByParty(partyEntity);
        if (argyleEntity != null)
            return argyleEntity.getUserToken();
        else
            return null;
    }

    public ArgyleEntity connectProspect(long prospectId, ArgyleConnectDTO argyleConnectDTO) {
        Optional<PartyEntity> party = this.partyService.getByCustomerId(prospectId);
        if (party.isPresent()) {
            ArgyleEntity argyleEntity = this.argyleRepository.findByParty(party.get());
            if (argyleEntity == null) {
                argyleEntity = new ArgyleEntity();
                argyleEntity.setParty(party.get());
            }
            argyleEntity.setUserId(argyleConnectDTO.getUserId());
            argyleEntity.setUserToken(argyleConnectDTO.getUserToken());
            ArgyleEntity result = argyleRepository.save(argyleEntity);
            this.partyService.updateConnectedAggregator(prospectId, AggregatorTypeEnum.ARGYLE);
            return result;
        } else {
            throw new GeneralHttpException("error", "Prospect not found !");
        }
    }

    public ArgyleProspectProfileModel getUserDetails(long prospectId) {
        String userId = this.getArgyleUserId(prospectId);
        if (userId == null)
            throw new GeneralHttpException("error", "Prospect has no argyle account mapped !");

        Map<String, String> params = new HashMap<String, String>();
        params.put("user", userId);
        params.put("offset", "0");
        params.put("limit", "9999");

        JSONObject results = this.getFromArgyle(params);
        ArgyleProspectProfileModel responseModel = new ArgyleProspectProfileModel();
        if (results == null)
            throw new GeneralHttpException("error", "Argyle profile fetch failed !");

        JSONArray profiles = results.getJSONArray("results");
        if (profiles.length() > 0) {
            try {
                JSONObject profile = profiles.getJSONObject(0);
                responseModel.setFirstName(profile.getString("first_name"));
                responseModel.setLastName(profile.getString("last_name"));
                responseModel.setDateOfBirth(profile.getString("birth_date"));
                responseModel.setEmail(profile.getString("email"));
                responseModel.setPhone(profile.getString("phone_number"));
                responseModel.setAddressLine1(profile.getJSONObject("address").getString("line1"));
                responseModel.setAddressLine2(profile.getJSONObject("address").optString("line2", null));
                responseModel.setCity(profile.getJSONObject("address").optString("city", null));
                responseModel.setState(profile.getJSONObject("address").optString("state", null));
                responseModel.setPostalCode(profile.getJSONObject("address").optString("postal_code", null));
                responseModel.setCountry(profile.getJSONObject("address").optString("country", null));
                responseModel.setSsn(profile.getString("ssn"));
                responseModel.setMaritalStatus(profile.getString("marital_status"));
                responseModel.setGender(profile.getString("gender"));
            } catch (Exception ex) {
                throw new GeneralHttpException("error", "Argyle profile parsing failed !");
            }

            ArrayList<EmploymentModel> employment = new ArrayList<EmploymentModel>();
            for (int i = 0; i < profiles.length(); i++) {
                JSONObject profile = profiles.getJSONObject(i);
                EmploymentModel model = new EmploymentModel();
                model.setJobTitle(profile.optString("job_title",""));
                model.setEmployer(profile.optString("employer",""));
                model.setHiredOn(profile.getJSONArray("hire_dates").join(","));
                model.setStatus(profile.optString("employment_status",""));
                model.setType(profile.optString("employment_type",""));
                employment.add(model);
            }
            responseModel.setEmployment(employment);
            // employment
        } else {
            throw new GeneralHttpException("error", "No profiles found !");
        }

        return responseModel;
    }

    public String getArgyleUserId(long prospectId) {
        PartyEntity partyEntity = new PartyEntity();
        partyEntity.setCustomerId(prospectId);
        ArgyleEntity argyleEntity = this.argyleRepository.findByParty(partyEntity);
        if (argyleEntity != null)
            return argyleEntity.getUserId();
        else
            return null;
    }

    public String syncArgyleEmployers() {
        BigDecimal count = new BigDecimal(0);
        JSONObject results = this.getLinkItems();
        JSONArray employers = results.getJSONArray("results");
        if (employers.length() > 0) {
            for (int i = 0; i < employers.length(); i++) {
                JSONObject emp = employers.getJSONObject(i);

                EmployersEntity model = new EmployersEntity();
                if (!emp.getBoolean("is_disabled")) {
                    EmployersEntity existingEmployer = employersRepository.findByName(emp.getString("name"));
                    if (existingEmployer == null) {
                        model.setName(emp.getString("name"));
                        model.setArgyleId(emp.getString("id"));
                        model.setArgyleType(emp.getString("type"));
                        employersRepository.save(model);
                    } else {
                        existingEmployer.setArgyleId(emp.getString("id"));
                        existingEmployer.setArgyleType(emp.getString("type"));
                        employersRepository.save(existingEmployer);
                    }
                    count = count.add(new BigDecimal(1));
                }
            }
        } else {
            throw new GeneralHttpException("error", "No results found !");
        }
        return count + " records synced";
    }

    public JSONObject getArgylePayAllocation(String userId) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(this.getBasicAuthHeader());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(argyleBasePath + "pay-allocations")
                .queryParam("user", userId);
        String result = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class)
                .getBody();
        try {
            return new JSONObject(result);
        } catch (JSONException err) {
            return null;
        }
    }

    private JSONObject getLinkItems() {
        HttpEntity<String> requestEntity = new HttpEntity<String>(this.getBasicAuthHeader());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(argyleBasePath + "link-items")
                .queryParam("offset", "0").queryParam("limit", "20000");
        String result = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class)
                .getBody();
        try {
            return new JSONObject(result);
        } catch (JSONException err) {
            return null;
        }
    }

    private JSONObject getFromArgyle(Map<String, String> params) {
        HttpEntity<String> request = new HttpEntity<String>(this.getBasicAuthHeader());
        String result = restTemplate
                .exchange(argyleBasePath + "profiles", HttpMethod.GET, request, String.class, params).getBody();
        try {
            return new JSONObject(result);
        } catch (JSONException err) {
            return null;
        }
    }

    private JSONObject postFromArgyle(String endpoint, JSONObject jsonObject) {
        HttpEntity<String> request = new HttpEntity<String>(jsonObject.toString(), this.getBasicAuthHeader());
        String result = this.restTemplate.postForObject(argyleBasePath + endpoint, request, String.class);
        try {
            return new JSONObject(result);
        } catch (JSONException err) {
            return null;
        }
    }

    private HttpHeaders getBasicAuthHeader() {
        byte[] plainCredsBytes = argyleToken.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    public ArgyleEntity getArgyleByParty(long prospectId) {
        PartyEntity partyEntity = new PartyEntity();
        partyEntity.setCustomerId(prospectId);
        return this.argyleRepository.findByParty(partyEntity);
    }

    public JSONObject getAccountsById(String user) {
        HttpEntity<String> requestEntity = new HttpEntity<String>(this.getBasicAuthHeader());
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(argyleBasePath + "accounts")
                .queryParam("user", user).queryParam("limit", "100");
        String result = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, requestEntity, String.class)
                .getBody();
        try {
            return new JSONObject(result);
        } catch (JSONException err) {
            return null;
        }
    }
    

    // public String getPayAllocationDetails(String accountId){

    // }

}
