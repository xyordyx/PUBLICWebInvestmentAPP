package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.Transient;
import java.util.Date;

public class InvoiceTransactions {
    @Transient
    private String status;
    @Transient
    private int minimumDuration;
    @Transient
    private double tem;
    @Transient
    private double tea;
    @Transient
    private Date paymentDate;
    @Transient
    private Date emissionDate;
    @Transient
    private Date createdAt;
    @Transient
    private String _id;
    @Transient
    private String currency;
    @Transient
    private String toBeCollectedIn;
    @Transient
    private Date actualPaymentDate;
    @Transient
    private Debtor debtor;
    @Transient
    private long pastDueDays;
    @Transient
    private int moraDays;

    public int getMoraDays() {
        return moraDays;
    }

    public void setMoraDays(int moraDays) {
        this.moraDays = moraDays;
    }

    @JsonGetter("pastDueDays")
    public long getPastDueDays() {
        return pastDueDays;
    }

    public void setPastDueDays(long pastDueDays) {
        this.pastDueDays = pastDueDays;
    }

    @JsonGetter("actualPaymentDate")
    public Date getActualPaymentDate() {
        return actualPaymentDate;
    }

    public void setActualPaymentDate(Date actualPaymentDate) {
        this.actualPaymentDate = actualPaymentDate;
    }

    @JsonGetter("debtor")
    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    @JsonGetter("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @JsonGetter("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonGetter("minimumDuration")
    public int getMinimumDuration() {
        return minimumDuration;
    }

    public void setMinimumDuration(int minimumDuration) {
        this.minimumDuration = minimumDuration;
    }

    @JsonGetter("tem")
    public double getTem() {
        return tem;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    @JsonGetter("tea")
    public double getTea() {
        return tea;
    }

    public void setTea(double tea) {
        this.tea = tea;
    }

    @JsonGetter("paymentDate")
    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @JsonGetter("emissionDate")
    public Date getEmissionDate() {
        return emissionDate;
    }

    public void setEmissionDate(Date emissionDate) {
        this.emissionDate = emissionDate;
    }

    @JsonGetter("_id")
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @JsonGetter("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonGetter("toBeCollectedIn")
    public String getToBeCollectedIn() {
        return toBeCollectedIn;
    }

    public void setToBeCollectedIn(String toBeCollectedIn) {
        this.toBeCollectedIn = toBeCollectedIn;
    }
}
