package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import javax.persistence.*;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;
import com.paywallet.borrowerservice.entities.enums.PhoneTypeEnum;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "Phone")
@Getter
@Setter
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long phoneId;

    @ManyToOne(cascade = CascadeType.PERSIST,targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity party;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "appNumber")
    private LeadProspectCampaignEntity app;

    @Column(nullable = true)
    private String sourceSystem;

    @Column(nullable = true)
    private long tenantId;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private PhoneTypeEnum phoneType;

    @Column()
    private ChoiceList isPrimary;

    @Column(unique = true)
    private String phoneNum;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}

