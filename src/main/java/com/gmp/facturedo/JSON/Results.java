package com.gmp.facturedo.JSON;

import javax.persistence.Transient;
import java.util.Date;

public class Results {
    @Transient
    private int id;
    @Transient
    private double percentage_completed;
    @Transient
    private double max_monthly_discount_rate;
    @Transient
    private Operation operation;
    @Transient
    private Debtor debtor;
    @Transient
    private Date investment_date;
    @Transient
    private double amount;
    @Transient
    private double discount_rate;
    @Transient
    private Date payment_date;
    @Transient
    private long toBeCollectedIn;
    @Transient
    private long totalInvestedDays;
    @Transient
    private double profit;
    @Transient
    private int transaction_type;
    @Transient
    private int status;
    @Transient
    private String currency;
    @Transient
    private BusinessRel businessRel;

    public BusinessRel getBusinessRel() {
        return businessRel;
    }

    public void setBusinessRel(BusinessRel businessRel) {
        this.businessRel = businessRel;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public long getTotalInvestedDays() {
        return totalInvestedDays;
    }

    public void setTotalInvestedDays(long totalInvestedDays) {
        this.totalInvestedDays = totalInvestedDays;
    }

    public long getToBeCollectedIn() {
        return toBeCollectedIn;
    }

    public void setToBeCollectedIn(long toBeCollectedIn) {
        this.toBeCollectedIn = toBeCollectedIn;
    }

    public Date getInvestment_date() {
        return investment_date;
    }

    public void setInvestment_date(Date investment_date) {
        this.investment_date = investment_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(double discount_rate) {
        this.discount_rate = discount_rate;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPercentage_completed() {
        return percentage_completed;
    }

    public void setPercentage_completed(double percentage_completed) {
        this.percentage_completed = percentage_completed;
    }

    public double getMax_monthly_discount_rate() {
        return max_monthly_discount_rate;
    }

    public void setMax_monthly_discount_rate(double max_monthly_discount_rate) {
        this.max_monthly_discount_rate = max_monthly_discount_rate;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Debtor getDebtor() {
        return debtor;
    }

    public void setDebtor(Debtor debtor) {
        this.debtor = debtor;
    }
}
