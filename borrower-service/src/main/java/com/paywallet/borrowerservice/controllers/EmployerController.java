package com.paywallet.borrowerservice.controllers;

import java.util.List;

import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.models.pinwheel.PinwheelLinkTokensModel;
import com.paywallet.borrowerservice.services.ArgyleService;
import com.paywallet.borrowerservice.services.EmployerService;
import com.paywallet.borrowerservice.services.PinwheelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    ArgyleService argyleService;

    @Autowired
    EmployerService empService;
    
    @Autowired
    private PinwheelService pinwheelService;

    @GetMapping("/")
    public List<EmployersEntity> getEmployersList(@RequestParam(required = false) String query) {
        return empService.getEmployersList(query);
    }

    @GetMapping("/{id}")
    public String getEmployersWithID(@PathVariable Long id) {
        return empService.getEmployersWithID(id);
    }

    @GetMapping("/sync/argyle")
    public String synchronizeArgyle() {
        return argyleService.syncArgyleEmployers();
    }
    
    @GetMapping("/sync/pinwheel")
    public GeneralHttpResponse<String> syncPinwheel() {
    	return pinwheelService.syncPinwheel();
    }
    
}