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
@Table(name = "FineractCustomer")
@Getter
@Setter
public class FineractCustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long fineractCustomrId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity party;

    @Column()
    long officeId;

    @Column()
    long clientId;

    @Column()
    long savingsId;

    @Column()
    long resourceId;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}
