package com.paywallet.borrowerservice.repositories;

import com.paywallet.borrowerservice.entities.customer.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long>,JpaSpecificationExecutor<EmailEntity> {
    public EmailEntity findByEmailId(String emailId);
}