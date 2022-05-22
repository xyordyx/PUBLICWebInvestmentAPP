package com.gmp.hmviking;

import com.gmp.web.dto.Investment;

import javax.persistence.Transient;
import java.util.List;

public class InvestmentBlock {
    @Transient
    private List<Investment> investmentList;
    @Transient
    private String scheduledTime;
    @Transient
    private boolean isScheduled;
    @Transient
    private boolean scheduleStatus;
    @Transient
    private boolean transactionStatus;
    @Transient
    private String system;
    @Transient
    private boolean sleep;

    public boolean isSleep() {
        return sleep;
    }

    public void setSleep(boolean sleep) {
        this.sleep = sleep;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public boolean isTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public List<Investment> getInvestmentList() {
        return investmentList;
    }

    public void setInvestmentList(List<Investment> investmentList) {
        this.investmentList = investmentList;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }

    public boolean isScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(boolean scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }
}
