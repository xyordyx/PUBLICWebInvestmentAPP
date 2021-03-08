package com.gmp.facturedo.JSON;

import javax.persistence.Transient;
import java.util.Date;

public class Operation {
    @Transient
    private String id;
    @Transient
    private String currency;
    @Transient
    private int cost_time_priority;
    @Transient
    private Date payment_date;
    @Transient
    private int status;
    @Transient
    private String business_relationship;

    public String getBusiness_relationship() {
        return business_relationship;
    }

    public void setBusiness_relationship(String business_relationship) {
        this.business_relationship = business_relationship;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getCost_time_priority() {
        return cost_time_priority;
    }

    public void setCost_time_priority(int cost_time_priority) {
        this.cost_time_priority = cost_time_priority;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }
}
