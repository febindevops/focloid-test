package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import javax.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "LeadProspectCampaign")
@Getter
@Setter
public class LeadProspectCampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long appNumber;

    @Column()
    private String sourceSystem;

    @Column()
    private long tenantId;

    @Column()
    private long campaignId;

    @Column()
    private long productId;

    @Column()
    private String channel;

    @Column()
    private long channelId;

    @Column()
    private String ipAddress;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedOn;
}
