package com.paywallet.borrowerservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.models.OTPVerificationDTO;
import com.paywallet.borrowerservice.repositories.OTPVerificationRepository;

@Service
public class OTPVerificationServiceImpl implements OTPVerificationService{

		@Autowired
		private OTPVerificationRepository otpVerficationRepository;
		
		@Override
		public GeneralHttpResponse<String> verifyOTP(OTPVerificationDTO otp, long prospectId) {
			boolean isVerified = otpVerficationRepository.existsByOtpAndCustomerIdCustomerId(otp.getOtp(), prospectId);
			if (isVerified) {
				return new GeneralHttpResponse<>(0L, 1, false, "OTP verification successful", null);
			} else {
				return new GeneralHttpResponse<>(0L, 0, true, "OTP verification failed", null);
			}
		}

}
