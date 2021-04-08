package com.paywallet.borrowerservice.repositories;

import java.util.List;

import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployersRepository
        extends JpaRepository<EmployersEntity, Long>, JpaSpecificationExecutor<EmployersEntity> {
    public EmployersEntity findByName(String name);

    public EmployersEntity findByArgyleId(String argyleId);

    public List<EmployersEntity> findFirst10ByNameIgnoreCaseStartingWith(String name);

}