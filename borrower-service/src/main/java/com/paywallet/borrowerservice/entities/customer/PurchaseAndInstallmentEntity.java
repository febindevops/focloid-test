package com.paywallet.borrowerservice.entities.customer;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import com.paywallet.borrowerservice.entities.enums.AllocationStatusEnum;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "PurchaseAndInstallment")
@Getter
@Setter
public class PurchaseAndInstallmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long installmentId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity customer;

    @Column()
    private String itemPurchased;
    @Column()
    private String purchasedFrom;
    @Column()
    private BigDecimal purchaseAmount;
    @Column()
    private String currency;
    @Column()
    private BigDecimal taxAmount;
    @Column()
    private BigDecimal deliveryCharges;
    @Column()
    private BigDecimal totalCost;
    @Column()
    private long installmentMinNo;
    @Column()
    private long installmentMaxNo;
    
    @Column()
    @Enumerated(EnumType.STRING)
    private AllocationStatusEnum allocationStatus;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date firstInstallmentDate;

    @Column()
    @Temporal(TemporalType.DATE)
    private Date lastInstallmentDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}
