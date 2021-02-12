package com.gmp.finsmart.JSON;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinsmartData {
    @Transient
    private double solesAmountAvailable;
    @Transient
    private double dollarAmountAvailable;

    @Transient
    private double solesCurrentInvested;
    @Transient
    private double dollarCurrentInvested;

    @Transient
    private double solesTotalDeposited;
    @Transient
    private double dollarTotalDeposited;

    @Transient
    private double solesTotalProfit;
    @Transient
    private double dollarTotalProfit;

    @Transient
    private double solesProfitExpected;
    @Transient
    private double dollarProfitExpected;

    @Transient
    private double solesRetentions;
    @Transient
    private double dollarRetentions;

    @Transient
    private Map<String,Transactions> depositedIndex;
    @Transient
    private Map<String,Transactions> retentionsIndex;
    @Transient
    private Map<String,Transactions> totalProfitIndex;
    @Transient
    private Map<String,Transactions> currentInvestmentsIndex;

    public FinsmartData() {
        this.depositedIndex = new HashMap<String, Transactions>();
        this.retentionsIndex = new HashMap<String, Transactions>();
        this.totalProfitIndex = new HashMap<String, Transactions>();
        this.currentInvestmentsIndex = new HashMap<String, Transactions>();
    }

    public Map<String, Transactions> getCurrentInvestmentsIndex() {
        return currentInvestmentsIndex;
    }

    public void setCurrentInvestmentsIndex(Map<String, Transactions> currentInvestmentsIndex) {
        this.currentInvestmentsIndex = currentInvestmentsIndex;
    }

    public Map<String, Transactions> getTotalProfitIndex() {
        return totalProfitIndex;
    }

    public void setTotalProfitIndex(Map<String, Transactions> totalProfitIndex) {
        this.totalProfitIndex = totalProfitIndex;
    }

    public Map<String, Transactions> getRetentionsIndex() {
        return retentionsIndex;
    }

    public void setRetentionsIndex(Map<String, Transactions> retentionsIndex) {
        this.retentionsIndex = retentionsIndex;
    }

    public Map<String, Transactions> getDepositedIndex() {
        return depositedIndex;
    }

    public void setDepositedIndex(Map<String, Transactions> depositedIndex) {
        this.depositedIndex = depositedIndex;
    }

    public double getSolesRetentions() {
        return solesRetentions;
    }

    public void setSolesRetentions(double solesRetentions) {
        this.solesRetentions = solesRetentions;
    }

    public double getDollarRetentions() {
        return dollarRetentions;
    }

    public void setDollarRetentions(double dollarRetentions) {
        this.dollarRetentions = dollarRetentions;
    }

    public double getSolesAmountAvailable() {
        return solesAmountAvailable;
    }

    public void setSolesAmountAvailable(double solesAmountAvailable) {
        this.solesAmountAvailable = solesAmountAvailable;
    }

    public double getDollarAmountAvailable() {
        return dollarAmountAvailable;
    }

    public void setDollarAmountAvailable(double dollarAmountAvailable) {
        this.dollarAmountAvailable = dollarAmountAvailable;
    }

    public double getSolesCurrentInvested() {
        return solesCurrentInvested;
    }

    public void setSolesCurrentInvested(double solesCurrentInvested) {
        this.solesCurrentInvested = solesCurrentInvested;
    }

    public double getDollarCurrentInvested() {
        return dollarCurrentInvested;
    }

    public void setDollarCurrentInvested(double dollarCurrentInvested) {
        this.dollarCurrentInvested = dollarCurrentInvested;
    }

    public double getSolesTotalDeposited() {
        return solesTotalDeposited;
    }

    public void setSolesTotalDeposited(double solesTotalDeposited) {
        this.solesTotalDeposited = solesTotalDeposited;
    }

    public double getDollarTotalDeposited() {
        return dollarTotalDeposited;
    }

    public void setDollarTotalDeposited(double dollarTotalDeposited) {
        this.dollarTotalDeposited = dollarTotalDeposited;
    }

    public double getSolesTotalProfit() {
        return solesTotalProfit;
    }

    public void setSolesTotalProfit(double solesTotalProfit) {
        this.solesTotalProfit = solesTotalProfit;
    }

    public double getDollarTotalProfit() {
        return dollarTotalProfit;
    }

    public void setDollarTotalProfit(double dollarTotalProfit) {
        this.dollarTotalProfit = dollarTotalProfit;
    }

    public double getSolesProfitExpected() {
        return solesProfitExpected;
    }

    public void setSolesProfitExpected(double solesProfitExpected) {
        this.solesProfitExpected = solesProfitExpected;
    }

    public double getDollarProfitExpected() {
        return dollarProfitExpected;
    }

    public void setDollarProfitExpected(double dollarProfitExpected) {
        this.dollarProfitExpected = dollarProfitExpected;
    }
}
