package com.gmp.web.dto;

import com.gmp.facturedo.JSON.Results;
import com.gmp.finsmart.JSON.Opportunities;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

public class Investment {
    @NotNull
    private String invoiceNumber;
    @NotNull
    private String currency;
    @NotNull
    private double amount;
    @Transient
    private Opportunities opportunity;
    @Transient
    private Results results;
    @Transient
    private String status;
    @Transient
    private String message;
    @Transient
    private double adjustedAmount;
    @Transient
    private boolean autoAdjusted;
    @Transient
    private boolean completed;

    @Transient
    private String UIState;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Investment() {
        this.autoAdjusted = false;
    }

    public double getAdjustedAmount() {
        return adjustedAmount;
    }

    public void setAdjustedAmount(double adjustedAmount) {
        this.adjustedAmount = adjustedAmount;
    }

    public boolean isAutoAdjusted() {
        return autoAdjusted;
    }

    public void setAutoAdjusted(boolean autoAdjusted) {
        this.autoAdjusted = autoAdjusted;
    }

    public String getUIState() {
        return UIState;
    }

    public void setUIState(String UIState) {
        this.UIState = UIState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Opportunities getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunities opportunity) {
        this.opportunity = opportunity;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
