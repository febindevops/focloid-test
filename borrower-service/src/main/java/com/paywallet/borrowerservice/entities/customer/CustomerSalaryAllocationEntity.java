package com.paywallet.borrowerservice.entities.customer;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "CustomerSalaryAllocation")
@Getter
@Setter
public class CustomerSalaryAllocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerSalaryAllocationId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = PartyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity customer;

    // @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = PartyEntity.class)
    // @JoinColumn(name = "customerId", nullable = true)
    @Column(nullable = true)
    private long walletId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = EmployersEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employerPrimaryId", nullable = true)
    private EmployersEntity employer;

    @Column()
    private String sourceSystem;

    @Column()
    private long tenantId;

    @Column()
    private String salaryRoutingNumber;

    @Column()
    private String salaryAccountNumber;

    @Column(nullable = true)
    private BigDecimal salaryCredited;

    @Column(nullable = true)
    private double salaryPercentAllocatedForWallet;

    @Column(nullable = true)
    private String salaryAggregationAccountId;

    @Column(nullable = true)
    private Date salaryAggregationAllocationTerminationDate;

    @Column(nullable = true)
    private String salaryAcctDebitCardNumber;

    @Column(nullable = true)
    private Date salaryAcctDebitCardExpDate;

    @Column(nullable = true)
    private long salaryAcctDebitCardCVV;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}
