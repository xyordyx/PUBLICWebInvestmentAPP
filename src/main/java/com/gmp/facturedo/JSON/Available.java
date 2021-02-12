package com.gmp.facturedo.JSON;

import javax.persistence.Transient;

public class Available {
    @Transient
    private double PEN;
    @Transient
    private double USD;

    public double getPEN() {
        return PEN;
    }

    public void setPEN(double PEN) {
        this.PEN = PEN;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }
}
