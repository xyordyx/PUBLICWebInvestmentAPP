package com.gmp.persistence.model;


import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class LastInvestmentsStatus {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String invoiceNumber;
    private String currency;
    private double amount;
    private String status;
    private String message;
    private double adjustedAmount;
    private boolean autoAdjusted;
    private String razonSocial;
    private String systemId;

    private String dateTime;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM - HH:mm");
        this.dateTime = dateTime.format(formatter);
    }
}
