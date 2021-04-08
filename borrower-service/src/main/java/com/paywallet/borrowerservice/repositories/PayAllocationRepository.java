package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.ArgylePayrollCacheEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PayAllocationRepository
        extends JpaRepository<ArgylePayrollCacheEntity, Long>, JpaSpecificationExecutor<ArgylePayrollCacheEntity> {
    ArgylePayrollCacheEntity findByAccountId(String accountId);
}
