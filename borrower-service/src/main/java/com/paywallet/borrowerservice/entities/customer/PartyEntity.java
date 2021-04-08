package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.paywallet.borrowerservice.entities.enums.AggregatorTypeEnum;
import com.paywallet.borrowerservice.entities.enums.CustomerTypeEnum;
import com.paywallet.borrowerservice.entities.enums.IdentificationTypeEnum;
import com.paywallet.borrowerservice.entities.enums.PartyRoleEnum;
import com.paywallet.borrowerservice.entities.customer.PartyEmployerEntity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Getter // Lombok
@Setter // Lombok
@Table(name = "Party", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "identificationNumber", "identificationType" }) })
public class PartyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "appNumber", nullable = true)
    private LeadProspectCampaignEntity appNumber;

    @Column(nullable = true)
    private String sourceSystem;

    @Column(nullable = true)
    private long tenantId;

    @Column()
    @Enumerated(EnumType.STRING)
    private PartyRoleEnum partyRole;

    @Column()
    @Enumerated(EnumType.STRING)
    private CustomerTypeEnum customerType;

    @Column()
    private String firstName;

    @Column()
    private String middleName;

    @Column()
    private String lastName;

    @Column(nullable = true)
    private String suffix;

    @Column(nullable = true)
    private String prefix;

    @Column(nullable = true)
    private String entityName;

    @Temporal(TemporalType.DATE)
    @Column()
    private Date dateOfBirth;

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    private Date dateOfIncorporation;

    // Make as a composite unique
    @Column(nullable = true)
    private String identificationNumber;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private IdentificationTypeEnum identificationType;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private AggregatorTypeEnum aggregatorConnected;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedOn;

    // @ManyToMany(cascade = { CascadeType.ALL })
    // @JoinTable(name = "Party_Employer", joinColumns = { @JoinColumn(name =
    // "customerId") }, inverseJoinColumns = {
    // @JoinColumn(name = "employerPrimaryId") })
    // private Set<EmployersEntity> employers = new HashSet<>();

    @OneToMany(mappedBy = "employer", cascade = { CascadeType.MERGE })
    private Set<PartyEmployerEntity> employers = new HashSet<PartyEmployerEntity>();
}
