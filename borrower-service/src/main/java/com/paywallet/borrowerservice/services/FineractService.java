package com.paywallet.borrowerservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paywallet.borrowerservice.common.GeneralHttpException;
import com.paywallet.borrowerservice.models.fineract.FineractCreateCustomerDTO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.apache.tomcat.util.codec.binary.Base64;

@Service
public class FineractService {
    private static final Logger logger = LogManager.getLogger(FineractService.class);
    @Autowired
    RestTemplate restTemplate;

    @Value("${fineract.api.base}")
    String fineractBasePath;

    @Value("${fineract.api.username}")
    String fineractUserName;

    @Value("${fineract.api.password}")
    String fineractPassword;

    @Value("${fineract.api.locale}")
    String fineractLocale;

    @Value("${fineract.api.dateFormat}")
    String fineractDateformat;

    public JSONObject createCustomer(FineractCreateCustomerDTO customerDTO) {

        customerDTO.setDateFormat(this.fineractDateformat);
        customerDTO.setLocale(this.fineractLocale);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestString = objectMapper.writeValueAsString(customerDTO);
            System.out.println(requestString);
            return this.fineractPostApi("clients?tenantIdentifier=default&pretty=true", requestString);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private JSONObject fineractPostApi(String endpoint, String jsonRequestBody) {

        HttpEntity<String> request = new HttpEntity<String>(jsonRequestBody.toString(), this.getBasicAuthHeader());
        try {
        String result = this.restTemplate.postForObject(this.fineractBasePath + endpoint, request, String.class);
            return new JSONObject(result);
        } catch (JSONException err) {
            throw new GeneralHttpException("error", "Finarect response parsing failed !");
        } catch (HttpClientErrorException ex) {
            throw new GeneralHttpException("error", "Error while communicating with finarect API.");
        }
    }

    private HttpHeaders getBasicAuthHeader() {
        byte[] plainCredsBytes = (fineractUserName + ":" + fineractPassword).getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

}
