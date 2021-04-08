package com.paywallet.borrowerservice.services;

import java.util.List;
import java.util.Optional;

import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.repositories.EmployersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployerService {

    @Autowired
    private EmployersRepository empRepository;

    public List<EmployersEntity> getEmployersList(String query) {
        if (query == null)
            query = "";
        return empRepository.findFirst10ByNameIgnoreCaseStartingWith(query);
    }

    public String getEmployersWithID(Long id) {
        return empRepository.findById(id).get().getArgyleId();
    }

    public EmployersEntity getEmployerByArgyleId(String argyleID) {
        return empRepository.findByArgyleId(argyleID);
    }

		public List<EmployersEntity> getAllEmployers() {
			return empRepository.findAll();
		}

		public void saveAllEmployers(List<EmployersEntity> employersEntityList) {
			empRepository.saveAll(employersEntityList);
		}

		public Optional<EmployersEntity> getEmployerById(long employerId) {
			return empRepository.findById(employerId);
		}
}
