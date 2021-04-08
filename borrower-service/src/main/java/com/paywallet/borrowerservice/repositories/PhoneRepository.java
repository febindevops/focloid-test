package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.PhoneEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<PhoneEntity, Long>,JpaSpecificationExecutor<PhoneEntity> {
    public PhoneEntity findByPhoneNum(String phoneNum);
}