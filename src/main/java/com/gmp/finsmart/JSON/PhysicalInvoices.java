package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.Transient;

public class PhysicalInvoices {
    @Transient
    private double totalAmount;
    @Transient
    private String code;

    @JsonGetter("totalAmount")
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @JsonGetter("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
