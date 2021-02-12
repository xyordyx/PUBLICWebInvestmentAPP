package com.gmp.facturedo.JSON;

import com.gmp.facturedo.FacturedoCIG;
import com.gmp.hmviking.LoginJSON;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class FacturedoData {
    @Transient
    private LoginJSON loginJSON;
    @Transient
    private double solesAmountAvailable;
    @Transient
    private double dollarAmountAvailable;

    @Transient
    private double solesCurrentInvested;
    @Transient
    private double dollarCurrentInvested;

    @Transient
    private double solesTotalProfit;
    @Transient
    private double dollarTotalProfit;

    @Transient
    private double solesProfitExpected;
    @Transient
    private double dollarProfitExpected;

    @Transient
    private List<Results> resultsInProgress;


    @Transient
    private Balance balanceJSON;
    @Transient
    private Invested investedJSON;
    @Transient
    private Auctions auctionsJSON;

    public FacturedoData(LoginJSON loginJSON) {
        this.loginJSON = loginJSON;
        this.balanceJSON = FacturedoCIG.getBalance(loginJSON);
        this.solesAmountAvailable = this.balanceJSON.getAvailable().getPEN();
        this.dollarAmountAvailable = this.balanceJSON.getAvailable().getUSD();
        this.solesCurrentInvested = this.balanceJSON.getInvested().getPEN();
        this.dollarCurrentInvested = this.balanceJSON.getInvested().getUSD();
        this.resultsInProgress = new ArrayList<>();
    }

    public LoginJSON getLoginJSON() {
        return loginJSON;
    }

    public void setLoginJSON(LoginJSON loginJSON) {
        this.loginJSON = loginJSON;
    }

    public Auctions getAuctionsJSON() {
        return auctionsJSON;
    }

    public void setAuctionsJSON(Auctions auctionsJSON) {
        this.auctionsJSON = auctionsJSON;
    }

    public Balance getBalanceJSON() {
        return balanceJSON;
    }

    public void setBalanceJSON(Balance balanceJSON) {
        this.balanceJSON = balanceJSON;
    }

    public Invested getInvestedJSON() {
        return investedJSON;
    }

    public void setInvestedJSON(Invested investedJSON) {
        this.investedJSON = investedJSON;
    }

    public List<Results> getResultsInProgress() {
        return resultsInProgress;
    }

    public void setResultsInProgress(List<Results> resultsInProgress) {
        this.resultsInProgress = resultsInProgress;
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
