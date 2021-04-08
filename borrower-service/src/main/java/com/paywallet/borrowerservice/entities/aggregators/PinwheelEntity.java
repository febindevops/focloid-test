package com.paywallet.borrowerservice.entities.aggregators;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

import com.paywallet.borrowerservice.entities.customer.PartyEntity;
import com.paywallet.borrowerservice.entities.enums.PinwheelTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@Entity
@Table(name = "Pinwheel")
@Getter
@Setter
@AllArgsConstructor
public class PinwheelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long pinwheelPrimaryId;

	@OneToOne(cascade = CascadeType.PERSIST, targetEntity = PartyEntity.class)
	@JoinColumn(name = "customerId", nullable = false)
	private PartyEntity party;

	@ManyToOne(cascade = CascadeType.PERSIST, targetEntity = EmployersEntity.class)
	@JoinColumn(name = "employerId", nullable = false)
	private EmployersEntity employer;

	@Column(length = 1024, nullable = false)
	private String tokenId;

	@Column(nullable = true)
	private String accountId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PinwheelTypeEnum type;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date lastUpdatedDate;
	
}
