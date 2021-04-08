package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = "Otp")
@Getter
@Setter
public class OTPVerificationEntity {
		
		public OTPVerificationEntity(PartyEntity customerId, Integer otp, String otpType, Date createdDate) {
			super();
			this.customerId = customerId;
			this.otp = otp;
			this.otpType = otpType;
			this.createdDate = createdDate;
		}

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long otpId;
		
	  @ManyToOne(cascade = CascadeType.PERSIST,targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = false)
		private PartyEntity customerId;
	  
	  @Column(nullable = false)
	  private Integer otp;
	  
	  @Column(nullable = false)
		private String otpType;
	  
	  @Column(nullable = false)
		private Date createdDate;
	
}
