package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.CustomerDebitCardAllocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDebitCardAllocationRepository extends JpaRepository<CustomerDebitCardAllocationEntity, Long>,
        JpaSpecificationExecutor<CustomerDebitCardAllocationEntity> {
}
