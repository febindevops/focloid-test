package com.paywallet.borrowerservice.services;

import java.util.List;
import java.util.Optional;

import com.paywallet.borrowerservice.common.GeneralHttpResponse;
import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.customer.PurchaseAndInstallmentEntity;
import com.paywallet.borrowerservice.entities.enums.AllocationStatusEnum;
import com.paywallet.borrowerservice.models.PurchaseAndInstallmentDTO;
import com.paywallet.borrowerservice.repositories.PurchaseAndInstallmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseAndInstallmentService {

    @Autowired
    private PurchaseAndInstallmentRepository repository;

    public GeneralHttpResponse<String> purchaseAndInstallment(PurchaseAndInstallmentDTO purchaseAndInstallment,
            Long customerId) {
        GeneralHttpResponse<String> res = new GeneralHttpResponse<String>();
        PurchaseAndInstallmentEntity installment = new PurchaseAndInstallmentEntity();
        PartyEntity party = new PartyEntity();
        party.setCustomerId(customerId);
        installment.setCustomer(party);
        installment.setItemPurchased(purchaseAndInstallment.getItemPurchased());
        installment.setPurchasedFrom(purchaseAndInstallment.getPurchasedFrom());
        installment.setPurchaseAmount(purchaseAndInstallment.getPurchaseAmount());
        installment.setCurrency(purchaseAndInstallment.getCurrency());
        installment.setTaxAmount(purchaseAndInstallment.getTaxAmount());
        installment.setDeliveryCharges(purchaseAndInstallment.getDeliveryCharges());
        installment.setTotalCost(purchaseAndInstallment.getTotalCost());
        installment.setInstallmentMinNo(purchaseAndInstallment.getInstallmentMinNo());
        installment.setInstallmentMaxNo(purchaseAndInstallment.getInstallmentMaxNo());
        installment.setFirstInstallmentDate(purchaseAndInstallment.getFirstInstallmentDate());
        installment.setLastInstallmentDate(purchaseAndInstallment.getLastInstallmentDate());
        installment.setAllocationStatus(AllocationStatusEnum.PENDING);
        PurchaseAndInstallmentEntity installmentRes = repository.save(installment);
        res.setId(installmentRes.getInstallmentId());
        res.setMessage("Installment added successfully");
        return res;
    }

    public Optional<PurchaseAndInstallmentEntity> getInstallmentsWithInstallmentID(long installmentID) {
        return repository.findById(installmentID);
    }

    public List<PurchaseAndInstallmentEntity> getInstallments() {
        return repository.findAll();
    }
}
