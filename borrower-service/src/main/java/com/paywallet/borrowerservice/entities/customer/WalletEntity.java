package com.paywallet.borrowerservice.entities.customer;

import java.util.Date;
import javax.persistence.*;

import com.paywallet.borrowerservice.entities.enums.AccountRoleEnum;
import com.paywallet.borrowerservice.entities.enums.AccountTypeEnum;

import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor // Lombok
@AllArgsConstructor // Lombok
@Entity
@Table(name = "Wallet")
@Getter
@Setter
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long walletId;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = PartyEntity.class)
    @JoinColumn(name = "customerId", nullable = true)
    private PartyEntity customer;

    @Column(nullable = true)
    private String sourceSystem;

    @Column(nullable = true)
    private long tenantId;

    @Column()
    private long accountId;

    @Column()
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    @Column()
    @Enumerated(EnumType.STRING)
    private AccountRoleEnum accountRole;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdatedDate;

    @Transient
    public String formattedAccountNumber() {
        return String.format("%09d", this.accountId);
    }

    @Transient
    public String formattedWalletId() {
        return String.format("%09d", this.walletId);
    }
}
