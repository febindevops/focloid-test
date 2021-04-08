package com.paywallet.borrowerservice.entities.customer;

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
@Table(name = "ArgylePayrollCache")
@Getter
@Setter
public class ArgylePayrollCacheEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long argylePayrollCacheId;

    @Column()
    long prospectId;

    @Column()
    String payAllocationId;

    @Column(unique = true)
    String accountId;

    @Column()
    String routingNumber;

    @Column()
    String accountNumber;

    @Column()
    String accounttype;

    @Column()
    String allocationType;

    @Column()
    String employer;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}