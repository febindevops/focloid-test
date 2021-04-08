package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "CustomerDebitCardAllocation")
@Getter
@Setter
public class CustomerDebitCardAllocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerDebitCardAllocId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = PartyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity customer;

    @Column()
    private String sourceSystem;

    @Column()
    private long tenantId;

    @Column()
    private String salaryAcctDebitCardNumber;

    @Column()
    private Date salaryAcctDebitCardExpDate;

    @Column()
    private int salaryAcctDebitCardCVV;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}
