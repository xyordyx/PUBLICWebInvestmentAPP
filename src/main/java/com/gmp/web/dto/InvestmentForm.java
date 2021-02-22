package com.gmp.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class InvestmentForm {
    @NotNull @NotEmpty(message="Enter Invoice Number")
    private String invoiceNumber;
    @NotNull @NotEmpty(message="Select a currency")
    private String currency;
    @NotNull @NotEmpty(message="Enter amount")
    private String amount;
    @NotNull @NotEmpty
    private String scheduledTime;
    @NotNull @NotEmpty
    private String timeRequest;
    @NotNull @NotEmpty
    private boolean sleep;

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public String getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(String timeRequest) {
        this.timeRequest = timeRequest;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
