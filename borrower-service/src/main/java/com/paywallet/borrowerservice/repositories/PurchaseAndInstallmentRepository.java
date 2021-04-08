package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.PurchaseAndInstallmentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseAndInstallmentRepository extends JpaRepository<PurchaseAndInstallmentEntity, Long>,
        JpaSpecificationExecutor<PurchaseAndInstallmentEntity> {
}
