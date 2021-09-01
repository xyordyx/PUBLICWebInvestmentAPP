package com.gmp.facturedo.thread;

import com.gmp.facturedo.JSON.Results;
import com.gmp.hmviking.LoginJSON;
import com.gmp.hmviking.QueueStructure;
import com.gmp.persistence.model.User;
import com.gmp.service.IReportService;
import com.gmp.web.dto.Investment;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.Callable;

import static com.gmp.facturedo.FacturedoUtil.generateAndSubmit;
import static com.gmp.facturedo.FacturedoUtil.waitForInvoiceInvest;
import static com.gmp.hmviking.InvestmentUtil.*;

public class FacturedoInvestor implements Callable<Investment> {
    private QueueStructure queueStr;
    private Investment investment;
    private LoginJSON loginJSON;
    Instant start = null;
    private String scheduleTime;
    @Autowired
    private IReportService reportService;
    private User userId;
    private String systemId;
    private int timeRequest;
    private boolean flag;
    private HashMap<Integer, Results> auctionsMap;

    public FacturedoInvestor(QueueStructure queueStr, Investment investment, LoginJSON loginJSON, String scheduleTime,
                             IReportService reportService, User userId, String systemId,int timeRequest,
                             HashMap<Integer, Results> auctionsMap){
        this.queueStr = queueStr;
        this.investment = investment;
        this.loginJSON = loginJSON;
        this.scheduleTime = scheduleTime;
        this.reportService = reportService;
        this.userId = userId;
        this.systemId = systemId;
        this.timeRequest = timeRequest;
        this.flag = true;
        this.auctionsMap = auctionsMap;
    }

    @Override
    public Investment call() {
        if(scheduleTime != null) {
            System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " scheduled - " + getTime());
            try {
                Thread.sleep(timesDiff(scheduleTime));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " was awakened - " + getTime());
                flag = false;
            }
            scheduleTime = null;
        }
        System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+ " STARTED - " + getTime());
        start = Instant.now();
        while (flag) {
            if(!auctionsMap.isEmpty()){
                investment = waitForInvoiceInvest(auctionsMap, investment);
                if(investment.getResults() != null) {
                    investment = generateAndSubmit(investment,loginJSON);
                    if(investment.isCompleted()){
                        System.out.println("Customer " + investment.getResults().getDebtor().getOfficial_name()+" Invoice: "+
                                investment.getResults().getOperation().getId() + " - Status: " +
                                investment.getStatus() +" - Message: "+
                                investment.getMessage()+" - "+getTime());
                        queueStr.setActualSize(queueStr.getActualSize()-1);
                        //reportService.updateInvestmentStatus(investment,userId,systemId);
                        return investment;
                    }
                }
            }
            try {
                if(this.timeRequest <= 200){
                    Thread.sleep(this.timeRequest);
                }else Thread.sleep(this.timeRequest - 100);
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+": Investor stopped - " + getTime());
                queueStr.setActualSize(queueStr.getActualSize()-1);
                flag = false;
                break;
            }
            if(minutesElapsed(start, Instant.now()) >= 15){
                System.out.println(Thread.currentThread().getName()+": "+investment.getInvoiceNumber()+
                        " investor stopped after 15 minutes - "+getTime());
                investment.setStatus("false");
                investment.setMessage("Invoice could not be found");
                //reportService.updateInvestmentStatus(investment,userId,systemId);
                queueStr.setActualSize(queueStr.getActualSize()-1);
                return investment;
            }
        }
        return null;
    }
}


