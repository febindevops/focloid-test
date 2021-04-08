package com.paywallet.borrowerservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.paywallet.borrowerservice.entities.aggregators.PinwheelEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;

public interface PinwheelRepository extends JpaRepository<PinwheelEntity, Long>,JpaSpecificationExecutor<PinwheelEntity>{
    public PinwheelEntity findByParty(PartyEntity party);
}
