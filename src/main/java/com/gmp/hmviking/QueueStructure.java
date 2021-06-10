package com.gmp.hmviking;

import com.gmp.web.dto.Investment;

import javax.persistence.Transient;
import java.util.*;

public class QueueStructure {
    private int actualSize;
    private HashMap<String,Double> balance;
    private volatile List<Investment> investmentList;
    @Transient
    private boolean transactionStatus;
    @Transient
    private String system;

    public QueueStructure(int actualSize, double availableSoles, double availableDollar, String system) {
        this.actualSize = actualSize;
        this.balance = new HashMap<>();
        this.balance.put("pen",availableSoles);
        this.balance.put("usd",availableDollar);
        this.investmentList = new ArrayList<>();
        this.system = system;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public List<Investment> getInvestmentList() {
        return investmentList;
    }

    public void setInvestmentList(List<Investment> investmentList) {
        this.investmentList = investmentList;
    }

    public boolean isTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public HashMap<String, Double> getBalance() {
        return balance;
    }

    public void setBalance(HashMap<String, Double> balance) {
        this.balance = balance;
    }

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }




}
