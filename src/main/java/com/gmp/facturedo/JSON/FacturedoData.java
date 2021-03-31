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
    private double solesTotalTransferred;
    @Transient
    private double dollarTotalTransferred;

    @Transient
    private List<Results> resultsInProgress;


    @Transient
    private Balance balanceJSON;
    @Transient
    private Dashboard dashboard;

    public FacturedoData(LoginJSON loginJSON) {
        this.loginJSON = loginJSON;
        this.balanceJSON = FacturedoCIG.getBalance(loginJSON);
        this.solesAmountAvailable = this.balanceJSON.getAvailable().getPEN();
        this.dollarAmountAvailable = this.balanceJSON.getAvailable().getUSD();
        this.solesCurrentInvested = this.balanceJSON.getInvested().getPEN();
        this.dollarCurrentInvested = this.balanceJSON.getInvested().getUSD();
        this.dashboard = FacturedoCIG.getDashboard(loginJSON);
        if(this.dashboard.getPortfolio().getEstimatedProfit().getUSD() !=null)
        this.dollarProfitExpected = this.dashboard.getPortfolio().getEstimatedProfit().getUSD();
        this.solesProfitExpected = this.dashboard.getPortfolio().getEstimatedProfit().getPEN();
        if(this.dashboard.getRepaidBids().getProfit().getUSD() != null)
        this.dollarTotalProfit = this.dashboard.getRepaidBids().getProfit().getUSD();
        this.solesTotalProfit = this.dashboard.getRepaidBids().getProfit().getPEN();
        if(this.dashboard.getDepositsAmount().getPEN() != null){
            if(this.dashboard.getWithdrawalsAmount().getPEN() != null){
                this.solesTotalTransferred = this.dashboard.getDepositsAmount().getPEN() - this.dashboard.getWithdrawalsAmount().getPEN();
            }else this.solesTotalTransferred = this.dashboard.getDepositsAmount().getPEN();
        }
        if(this.dashboard.getDepositsAmount().getUSD() != null){
            if(this.dashboard.getWithdrawalsAmount().getUSD() != null){
                this.dollarTotalTransferred = this.dashboard.getDepositsAmount().getUSD() - this.dashboard.getWithdrawalsAmount().getUSD();
            }else this.dollarTotalTransferred = this.dashboard.getDepositsAmount().getUSD();
        }
        this.resultsInProgress = new ArrayList<>();
    }

    public LoginJSON getLoginJSON() {
        return loginJSON;
    }

    public void setLoginJSON(LoginJSON loginJSON) {
        this.loginJSON = loginJSON;
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

    public double getSolesTotalTransferred() {
        return solesTotalTransferred;
    }

    public void setSolesTotalTransferred(double solesTotalTransferred) {
        this.solesTotalTransferred = solesTotalTransferred;
    }

    public double getDollarTotalTransferred() {
        return dollarTotalTransferred;
    }

    public void setDollarTotalTransferred(double dollarTotalTransferred) {
        this.dollarTotalTransferred = dollarTotalTransferred;
    }

    public List<Results> getResultsInProgress() {
        return resultsInProgress;
    }

    public void setResultsInProgress(List<Results> resultsInProgress) {
        this.resultsInProgress = resultsInProgress;
    }

    public Balance getBalanceJSON() {
        return balanceJSON;
    }

    public void setBalanceJSON(Balance balanceJSON) {
        this.balanceJSON = balanceJSON;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
}
