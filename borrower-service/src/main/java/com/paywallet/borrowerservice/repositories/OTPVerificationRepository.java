package com.paywallet.borrowerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.paywallet.borrowerservice.entities.customer.OTPVerificationEntity;

public interface OTPVerificationRepository extends JpaRepository<OTPVerificationEntity, Long>,JpaSpecificationExecutor<OTPVerificationEntity> {

		boolean existsByOtpAndCustomerIdCustomerId(Integer otp, long parseLong);

}

