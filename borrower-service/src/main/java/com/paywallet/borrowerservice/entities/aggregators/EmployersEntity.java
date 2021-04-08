package com.paywallet.borrowerservice.entities.aggregators;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paywallet.borrowerservice.entities.customer.PartyEmployerEntity;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "Employer")
@Getter
@Setter
public class EmployersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long employerPrimaryId;

    @Column(unique = true, columnDefinition = "VARCHAR(1024) CHARACTER SET utf8 COLLATE utf8_bin")
    private String name;

    @Column(nullable = true)
    private String argyleId;

    @Column(nullable = true)
    private String pinwheelId;

    @Column()
    private String argyleType;

    // @ManyToMany(mappedBy = "employers")
    // @JsonIgnore
    // private Set<PartyEntity> parties = new HashSet<>();

    @OneToMany(mappedBy = "party", cascade = { CascadeType.MERGE })
    @JsonIgnore
    private Set<PartyEmployerEntity> parties = new HashSet<PartyEmployerEntity>();

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;
}
