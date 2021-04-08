package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import javax.persistence.*;

import com.paywallet.borrowerservice.entities.enums.AddressTypeEnum;
import com.paywallet.borrowerservice.entities.enums.ChoiceList;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "PostalAddress")
@Getter
@Setter
public class PostalAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postalId;

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
    @Enumerated(EnumType.STRING)
    private AddressTypeEnum addressType;

    @Column()
    @Enumerated(EnumType.STRING)
    private ChoiceList isPrimary;

    @Column()
    private String address1;

    @Column()
    private String address2;

    @Column()
    private String address3;

    @Column()
    private String city;

    @Column()
    private String zipcode;

    @Column()
    private String state;

    @Column()
    private String country;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}
