package com.gmp.finsmart.JSON;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.Transient;
import java.util.Date;

public class Transactions {
    @Transient
    private String status;
    @Transient
    private int v1IdInv;
    @Transient
    private String currency;
    @Transient
    private Double amount;
    @Transient
    private Invoice invoice;
    @Transient
    private String _id;
    @Transient
    private String type;
    @Transient
    private Date createdAt;
    @Transient
    private Double profit;


    @Transient
    private Double expectedProfit;
    @Transient
    private InvoiceTransactions invoiceAppend;

    @JsonGetter("expectedProfit")
    public Double getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(Double expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    @JsonGetter("profit")
    public Double getProfit() {
        return profit;
    }

    @JsonGetter("invoiceAppend")
    public InvoiceTransactions getInvoiceAppend() {
        return invoiceAppend;
    }

    public void setInvoiceAppend(InvoiceTransactions invoiceAppend) {
        this.invoiceAppend = invoiceAppend;
    }

    @JsonGetter("profit")
    public void setProfit(Double profit) {
        this.profit = profit;
    }

    @JsonGetter("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonGetter("v1Id")
    public int getV1IdInv() {
        return v1IdInv;
    }

    public void setV1IdInv(int v1IdInv) {
        this.v1IdInv = v1IdInv;
    }

    @JsonGetter("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonGetter("amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @JsonGetter("invoice")
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @JsonGetter("_id")
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @JsonGetter("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonGetter("createdAt")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
