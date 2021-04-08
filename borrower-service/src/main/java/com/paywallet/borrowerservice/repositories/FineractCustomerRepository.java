package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.FineractCustomerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FineractCustomerRepository
        extends JpaRepository<FineractCustomerEntity, Long>, JpaSpecificationExecutor<FineractCustomerEntity> {
}