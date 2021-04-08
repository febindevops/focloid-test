
package com.paywallet.borrowerservice.repositories;

import java.util.Set;

import com.paywallet.borrowerservice.entities.customer.PartyEmployerEntity;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyEmployerRepository
        extends JpaRepository<PartyEmployerEntity, Long>, JpaSpecificationExecutor<PartyEmployerEntity> {
    public Set<PartyEmployerEntity> findAllByParty(PartyEntity party);
}
