package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.CustomerSalaryAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSalayAllocationRepository extends JpaRepository<CustomerSalaryAllocationEntity, Long>,
                JpaSpecificationExecutor<CustomerSalaryAllocationEntity> {

        CustomerSalaryAllocationEntity findBySalaryAccountNumberAndSalaryAggregationAccountId(String accountNumber,
                        String aggregationAccountId);
}
