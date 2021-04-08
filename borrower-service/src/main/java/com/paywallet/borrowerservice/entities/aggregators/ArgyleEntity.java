package com.paywallet.borrowerservice.entities.aggregators;

import java.util.Date;
import javax.persistence.*;

import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "Argyle")
@Getter
@Setter
public class ArgyleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long argylePrimaryId;

    @OneToOne(cascade = CascadeType.PERSIST,targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity party;

    @Column()
    private String userId;

    @Column(length = 1024)
    private String userToken;

    // generate your new token here (more info: https://argyle.com/docs/api-reference/user-tokens)

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}

//Employer List needs to be added.
