package com.gmp.hmviking;

import com.gmp.web.dto.Investment;

import javax.persistence.Transient;
import java.util.*;

public class QueueStructure {
    private int actualSize;
    private volatile List<Investment> investmentList;
    @Transient
    private boolean transactionStatus;
    @Transient
    private String system;
    @Transient
    private boolean isScheduled;

    public QueueStructure(int actualSize, double availableSoles, double availableDollar, String system,
                          boolean scheduled) {
        this.actualSize = actualSize;
        this.investmentList = new ArrayList<>();
        this.system = system;
        this.isScheduled = scheduled;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
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

    public int getActualSize() {
        return actualSize;
    }

    public void setActualSize(int actualSize) {
        this.actualSize = actualSize;
    }




}
