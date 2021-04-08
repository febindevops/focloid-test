package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.enums.AggregatorTypeEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends JpaRepository<PartyEntity, Long>, JpaSpecificationExecutor<PartyEntity> {
    public PartyEntity findByCustomerId(long customerId);
}