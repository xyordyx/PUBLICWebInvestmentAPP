package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;
import java.util.List;

public class Opportunities {
    @Transient
    private double tem;
    @Transient
    private String currency;
    @Transient
    private int v1IdGroup;
    @Transient
    private String _id;
    @Transient
    private List<PhysicalInvoices> physicalInvoices;
    @Transient
    private double availableBalanceAmount;
    @Transient
    private String debtorCompanyName;
    @Transient
    private Debtor debtor;

    @JsonCreator
    public Opportunities(@JsonProperty("tem") double tem, @JsonProperty("currency") String currency,
                         @JsonProperty("v1IdGroup") int v1IdGroup, @JsonProperty("_id") String _id,
                         @JsonProperty("physicalInvoices")List<PhysicalInvoices> physicalInvoices,
                         @JsonProperty("availableBalanceAmount") double availableBalanceAmount,
                         @JsonProperty("debtorCompanyName") String debtorCompanyName, @JsonProperty("debtor")Debtor debtor) {
        this.tem = tem;
        this.currency = currency;
        this.v1IdGroup = v1IdGroup;
        this._id = _id;
        this.physicalInvoices = physicalInvoices;
        this.availableBalanceAmount = availableBalanceAmount;
        this.debtorCompanyName = debtorCompanyName;
        this.debtor = debtor;
    }

    @JsonGetter("debtor")
    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }

    @JsonGetter("debtorCompanyName")
    public String getDebtorCompanyName() {
        return debtorCompanyName;
    }

    public void setDebtorCompanyName(String debtorCompanyName) {
        this.debtorCompanyName = debtorCompanyName;
    }

    @JsonGetter("tem")
    public double getTem() {
        return tem;
    }

    public void setTem(double tem) {
        this.tem = tem;
    }

    @JsonGetter("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonGetter("v1IdGroup")
    public int getV1IdGroup() {
        return v1IdGroup;
    }

    public void setV1IdGroup(int v1IdGroup) {
        this.v1IdGroup = v1IdGroup;
    }

    @JsonGetter("_id")
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    @JsonGetter("physicalInvoices")
    public List<PhysicalInvoices> getPhysicalInvoices() {
        return physicalInvoices;
    }

    public void setPhysicalInvoices(List<PhysicalInvoices> physicalInvoices) {
        this.physicalInvoices = physicalInvoices;
    }

    @JsonGetter("availableBalanceAmount")
    public double getAvailableBalanceAmount() {
        return availableBalanceAmount;
    }

    public void setAvailableBalanceAmount(double availableBalanceAmount) {
        this.availableBalanceAmount = availableBalanceAmount;
    }
}