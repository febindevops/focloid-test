package com.paywallet.borrowerservice.entities.customer;

import java.io.Serializable;

public class PartyEmployerID implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    protected long party;
    protected long employer;

    public PartyEmployerID() {
    }

    public PartyEmployerID(long party, long employer) {
        this.party = party;
        this.employer = employer;
    }
}