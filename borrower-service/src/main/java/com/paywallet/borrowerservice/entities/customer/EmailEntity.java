package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import javax.persistence.*;

import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "Email")
@Getter
@Setter
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long emailPrimaryId;

    @ManyToOne(cascade = CascadeType.PERSIST,targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity party;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "appNumber")
    private LeadProspectCampaignEntity appNumber;

    @Column()
    private String sourceSystem;

    @Column()
    private long tenantId;

    @Column()
    private String emailType;

    @Column()
    private ChoiceList isPrimary;

    @Column(unique = true)
    private String emailId;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}

