package com.paywallet.borrowerservice.entities.customer;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.paywallet.borrowerservice.entities.aggregators.EmployersEntity;
import com.paywallet.borrowerservice.entities.enums.AggregatorTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Getter // Lombok
@Setter // Lombok
@IdClass(PartyEmployerID.class)
@Table(name = "PartyEmployer")
public class PartyEmployerEntity {

    @Id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "party")
    private PartyEntity party;

    @Id
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employer")
    private EmployersEntity employer;

    private AggregatorTypeEnum aggregatorType;
}
