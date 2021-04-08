package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.customer.WalletEntity;
import com.paywallet.borrowerservice.entities.enums.AccountTypeEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Long>, JpaSpecificationExecutor<WalletEntity> {
    @Query("SELECT we FROM WalletEntity we WHERE we.customer = ?1 and we.accountType = ?2")
    public WalletEntity findByAccountTypeAndCustomer(PartyEntity customer,AccountTypeEnum accountType);
}
