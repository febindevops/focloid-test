package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.customer.PostalAddressEntity;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostalAddressRepository
                extends JpaRepository<PostalAddressEntity, Long>, JpaSpecificationExecutor<PostalAddressEntity> {
        PostalAddressEntity findByParty(PartyEntity party);

        PostalAddressEntity findByPartyAndIsPrimary(PartyEntity party, ChoiceList isPrimary);
}