package com.paywallet.borrowerservice.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.paywallet.borrowerservice.common.GeneralHttpException;
import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.aggregators.PinwheelEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.enums.PinwheelTypeEnum;
import com.paywallet.borrowerservice.models.employers.EmploymentModel;
import com.paywallet.borrowerservice.models.pinwheel.PinwheelLinkTokensModel;
import com.paywallet.borrowerservice.models.pinwheel.PinwheelProspectProfileModel;
import com.paywallet.borrowerservice.repositories.PinwheelRepository;

@Service
public class PinwheelService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private EmployerService employerService;
	@Autowired
	private PartyService partyService;
	@Autowired
	private PinwheelRepository pinwheelRepository;

	@Value("${pinwheel.token}")
	private String pinwheelToken;
	@Value("${pinwheel.base}")
	private String pinwheelBasePath;
	@Value("${pinwheel.apiKey}")
	private String pinwheelApiKey;

	public static final String ERROR = "error";

	public GeneralHttpResponse<String> syncPinwheel() {
		JSONObject pinwheelEmployers = getPinwheelEmployers();
		List<EmployersEntity> employersList = employerService.getAllEmployers();
		List<EmployersEntity> employersEntityList = new ArrayList<>();
		JSONArray employers = pinwheelEmployers.getJSONArray("data");
		if (employers.length() > 0) {
			for (int i = 0; i < employers.length(); i++) {
				boolean isExist = false;
				JSONObject employee = employers.getJSONObject(i);
				for (EmployersEntity emp : employersList) {
					if (employee.getString("name").equalsIgnoreCase(emp.getName())) {
						employersEntityList.add(updateExistingEmployer(employee, emp));
						isExist = true;
					}
				}
				if (!isExist) {
					employersEntityList.add(setPinwheelEmployer(employee));
				}
			}
		} else {
			throw new GeneralHttpException(ERROR, "No results found !");
		}
		employerService.saveAllEmployers(employersEntityList);
		return new GeneralHttpResponse<>(0L, 1, true, "Pinwheel record synched successfully", null);
	}

	private EmployersEntity setPinwheelEmployer(JSONObject employee) {
		EmployersEntity empEntity = new EmployersEntity();
		empEntity.setName(employee.getString("name"));
		empEntity.setPinwheelId(employee.getString("id"));
		empEntity.setLastUpdatedDate(new Date());
		return empEntity;
	}

	private EmployersEntity updateExistingEmployer(JSONObject employee, EmployersEntity emp) {
		EmployersEntity empEntity = new EmployersEntity();
		empEntity.setEmployerPrimaryId(emp.getEmployerPrimaryId());
		empEntity.setName(emp.getName());
		empEntity.setArgyleId(emp.getArgyleId());
		empEntity.setPinwheelId(employee.getString("id"));
		empEntity.setArgyleType(emp.getArgyleType());
		empEntity.setLastUpdatedDate(new Date());
		return empEntity;
	}

	private JSONObject getPinwheelEmployers() {
		HttpHeaders httpHeaders = basicPinwheelheader();
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		Map<String, String> params = new HashMap<>();
		return new JSONObject(restTemplate
				.exchange(pinwheelBasePath + "employers", HttpMethod.GET, entity, String.class, params).getBody());
	}

	private HttpHeaders basicPinwheelheader() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.set(pinwheelApiKey, this.pinwheelToken);
		return httpHeaders;
	}

	public PinwheelLinkTokensModel linkToken(long employerId, long customerId) {
		Optional<EmployersEntity> employer = employerService.getEmployerById(employerId);
		PinwheelLinkTokensModel linktokenModel = new PinwheelLinkTokensModel();

		if (employer.isPresent() && employer.get().getPinwheelId() != null) {
			HttpEntity<String> request = new HttpEntity<>(getTokenBody(employer.get().getPinwheelId()).toString(),
					basicPinwheelheader());
			JSONObject jsonObject = new JSONObject(
					restTemplate.postForObject(pinwheelBasePath + "link_tokens", request, String.class));
			JSONObject pinwheelObject = jsonObject.getJSONObject("data");
			Optional<PartyEntity> party = partyService.getByCustomerId(customerId);
			linktokenModel.setMode(pinwheelObject.getString("mode"));
			linktokenModel.setExpires(pinwheelObject.getLong("expires"));
			linktokenModel.setTokenId(pinwheelObject.getString("token_id"));
			linktokenModel.setToken(pinwheelObject.getString("token"));
			linktokenModel.setId(pinwheelObject.getString("id"));

			Optional<EmployersEntity> employerFromId = employerService.getEmployerById(employerId);

			if (party.isPresent()) {
				PinwheelEntity pinwheel = new PinwheelEntity();
				pinwheel.setParty(party.get());
				pinwheel.setEmployer(employerFromId.get());
				pinwheel.setTokenId(linktokenModel.getTokenId());
				pinwheel.setType(PinwheelTypeEnum.INFO_COLLECTION);
				pinwheel.setEmployer(employerFromId.get());
				PinwheelEntity pinwheelRes = pinwheelRepository.save(pinwheel);
				linktokenModel.setPinWheelId(pinwheelRes.getPinwheelPrimaryId());
			} else {
				throw new GeneralHttpException(ERROR, "No customer found !");
			}
		} else if (!employer.isPresent()) {
			throw new GeneralHttpException(ERROR, "No employer found !");
		} else {
			throw new GeneralHttpException(ERROR, "No pinwheel ID found !");
		}
		return linktokenModel;
	}

	private JSONObject getTokenBody(String pinwheelId) {
		List<String> jobs = new ArrayList<>();
		jobs.add("income");
		jobs.add("employment");
		jobs.add("identity");
		jobs.add("paystubs");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("org_name", "Payroll Wallet");
		jsonObject.put("employer_id", pinwheelId);
		jsonObject.put("required_jobs", new JSONArray(jobs));
		return jsonObject;
	}

	public PinwheelEntity pinwheelUpdate(Long pinwheelId, String accountId) {
		Optional<PinwheelEntity> pinwheelOpt = pinwheelRepository.findById(pinwheelId);
		PinwheelEntity pinwheelRet;
		if (pinwheelOpt.isPresent()) {
			PinwheelEntity pinwheel = pinwheelOpt.get();
			pinwheel.setAccountId(accountId);
			pinwheelRet = pinwheelRepository.save(pinwheel);
		} else {
			throw new GeneralHttpException(ERROR, "No pinwheel entry found for the given ID !");
		}
		return pinwheelRet;
	}

	public PinwheelProspectProfileModel getUserDetails(long prospectId) throws JSONException {
		String accountId = this.getPinwheelUserId(prospectId);
		if (accountId == null)
			throw new GeneralHttpException(ERROR, "Prospect has no pinwheel account mapped !");
		String profileUrl = "accounts/" + accountId + "/identity";
		JSONObject results = this.getFromPinwheel(profileUrl, HttpMethod.GET, new HashMap<String, String>());

		PinwheelProspectProfileModel responseModel = new PinwheelProspectProfileModel();
		if (results == null)
			throw new GeneralHttpException(ERROR, "Pinwheel profile fetch failed !");

		JSONObject profile = results.getJSONObject("data");
		try {
			String fullname = profile.getString("full_name");
			if (fullname.contains(" ")) {
				int i = fullname.indexOf(" ");
				String firstName = fullname.substring(0, i);
				String lastName = fullname.substring(i + 1);
				responseModel.setFirstName(firstName);
				responseModel.setLastName(lastName);
			} else {
				responseModel.setFirstName(fullname);
			}

			responseModel.setEmail(profile.getJSONArray("emails").getString(0));
			responseModel.setPhone(profile.getJSONArray("phone_numbers").getJSONObject(0).getString("value"));
			responseModel.setDateOfBirth(profile.getString("date_of_birth"));
			responseModel.setSsn(profile.optString("last_four_ssn", ""));
			JSONObject address = profile.getJSONObject("address");
			responseModel.setCity(address.optString("city", ""));
			responseModel.setAddressLine1(address.optString("line1", ""));
			responseModel.setAddressLine2(address.optString("line2", ""));
			responseModel.setState(address.optString("state", ""));
			responseModel.setCountry(address.optString("country", ""));
			responseModel.setPostalCode(address.optString("postal_code", ""));

			String employmentUrl = "accounts/" + accountId + "/employment";
			JSONObject employmentResults = this.getFromPinwheel(employmentUrl, HttpMethod.GET,
					new HashMap<String, String>());
			responseModel.setEmployment(this.getEmploymentDetails(employmentResults));
		} catch (Exception ex) {
			throw new GeneralHttpException(ERROR, "Argyle profile parsing failed !");
		}
		return responseModel;
	}

	ArrayList<EmploymentModel> getEmploymentDetails(JSONObject employmentResults) {
		ArrayList<EmploymentModel> employment = new ArrayList<EmploymentModel>();
		try {
			JSONObject pinwheelEmp = employmentResults.getJSONObject("data");
			EmploymentModel model = new EmploymentModel();
			model.setEmployer(pinwheelEmp.optString("employer_name", ""));
			model.setHiredOn(pinwheelEmp.optString("start_date", ""));
			model.setStatus(pinwheelEmp.optString("status", ""));
			employment.add(model);
		} catch (NullPointerException nullexp) {
		}
		return employment;
	}

	public String getPinwheelUserId(long prospectId) {
		PartyEntity partyEntity = new PartyEntity();
		partyEntity.setCustomerId(prospectId);
		PinwheelEntity pinwheelEntity = this.pinwheelRepository.findByParty(partyEntity);
		if (pinwheelEntity != null)
			return pinwheelEntity.getAccountId();
		else
			return null;
	}

	private JSONObject getFromPinwheel(String endPoint, HttpMethod method, Map<String, String> params) {
		HttpEntity<String> request = new HttpEntity<String>(this.basicPinwheelheader());
		String result = restTemplate.exchange(pinwheelBasePath + endPoint, method, request, String.class, params)
				.getBody();
		try {
			return new JSONObject(result);
		} catch (JSONException err) {
			return null;
		}
	}
}
