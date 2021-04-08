package com.paywallet.borrowerservice.services;

import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.models.OTPVerificationDTO;

public interface OTPVerificationService {
	GeneralHttpResponse<String> verifyOTP(OTPVerificationDTO otp, long prospectId);
}
