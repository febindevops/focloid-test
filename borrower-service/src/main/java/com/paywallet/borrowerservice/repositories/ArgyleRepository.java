package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.aggregators.ArgyleEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArgyleRepository extends JpaRepository<ArgyleEntity, Long>,JpaSpecificationExecutor<ArgyleEntity> {
    public ArgyleEntity findByParty(PartyEntity party);
}
