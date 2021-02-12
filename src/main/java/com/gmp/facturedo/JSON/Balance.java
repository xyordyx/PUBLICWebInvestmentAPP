package com.gmp.facturedo.JSON;

import javax.persistence.Transient;

public class Balance {
    @Transient
    private Available available;
    @Transient
    private Invested invested;

    public Invested getInvested() {
        return invested;
    }

    public void setInvested(Invested invested) {
        this.invested = invested;
    }

    public Available getAvailable() {
        return available;
    }

    public void setAvailable(Available available) {
        this.available = available;
    }

}
